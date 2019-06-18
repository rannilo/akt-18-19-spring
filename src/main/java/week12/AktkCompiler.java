package week12;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import week11.AktkBinding;
import week9.AktkAst;
import week9.ast.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;


public class AktkCompiler {

    public static void main(String[] args) throws IOException {
        // lihtsam viis "käsurea parameetrite andmiseks":
        //args = new String[] {"yks_pluss_yks.aktk"};

        if (args.length != 1) {
            throw new IllegalArgumentException("Sellele programmile tuleb anda parameetriks kompileeritava AKTK faili nimi");
        }

        Path sourceFile = Paths.get(args[0]);
        if (!Files.isRegularFile(sourceFile)) {
            throw new IllegalArgumentException("Ei leia faili nimega '" + sourceFile + "'");
        }

        String className = sourceFile.getFileName().toString().replace(".aktk", "");
        Path classFile = sourceFile.toAbsolutePath().getParent().resolve(className + ".class");

        createClassFile(sourceFile, className, classFile);
    }

    private static void createClassFile(Path sourceFile, String className, Path classFile) throws IOException {
        // loen faili sisu muutujasse
        String source = new String(Files.readAllBytes(sourceFile), StandardCharsets.UTF_8);

        // parsin ja moodustan AST'i
        AstNode ast = AktkAst.createAst(source);

        // seon muutujad
        AktkBinding.bind(ast);

        // kompileerin
        byte[] bytes = createClass(ast, className);
        Files.write(classFile, bytes);
    }

    public static byte[] createClass(AstNode ast, String className) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        // Klassi attribuudid
        cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER, className, null, "java/lang/Object", null);
        cw.visitSource(null, null);

        // main meetod
        MethodVisitor mv = cw.visitMethod(
                ACC_PUBLIC + ACC_STATIC,                    // modifikaatorid
                "main",                                     // meetodi nimi
                "([Ljava/lang/String;)V",                   // meetodi kirjeldaja
                null,                                       // geneerikute info
                new String[]{"java/io/IOException"});
        mv.visitCode();
        // terve AKTK programm tuleb kompileerida main meetodi sisse
        new AktkCompilerVisitor(mv).visit(ast);
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();


        // klassi lõpetamine
        cw.visitEnd();

        // klassi baidijada genereerimine
        return cw.toByteArray();
    }

    private static class AktkCompilerVisitor extends AstVisitor.VoidVisitor {

        private final Map<VariableBinding, Integer> variableIndices = new HashMap<>();
        private final MethodVisitor mv;

        private AktkCompilerVisitor(MethodVisitor mv) {
            this.mv = mv;
        }

        private int getVariableIndex(VariableBinding binding) {
            Integer index = variableIndices.get(binding);
            if (index != null) {
                return index;
            } else {
                // muutuja 0 on meie main-meetodi parameeter

                index = variableIndices.size() + 1;
                variableIndices.put(binding, index);
                return index;
            }
        }

        @Override
        protected void visitVoid(Assignment assignment) {
            // 1) genereeri avaldise väärtustamise kood
            visit(assignment.getExpression());
            // 2) genereeri väärtuse muutujasse salvestamise kood
            int i = getVariableIndex(assignment.getBinding());
            mv.visitVarInsn(ISTORE, i);
        }

        @Override
        protected void visitVoid(Block block) {
            for (Statement stmt : block.getStatements()) {
                visit(stmt);
            }
        }

        @Override
        protected void visitVoid(ExpressionStatement expressionStatement) {
            // NB! Ära unusta, et ExpressionStatement viskab arvutatud väärtuse minema
            visit(expressionStatement.getExpression());
            mv.visitInsn(POP);
        }

        @Override
        protected void visitVoid(FunctionCall functionCall) {
            if (functionCall.getFunctionName().equals("-") && functionCall.getArguments().size() == 1) {
                visit(functionCall.getArguments().get(0));
                mv.visitInsn(INEG);
            } else if (functionCall.isArithmeticOperation()) {
                generateArithmeticOrLogicOperation(functionCall);
            } else if (functionCall.isComparisonOperation()) {
                generateComparisonOperation(functionCall);
            } else {
                generateNormalFunctionCall(functionCall);
            }
        }

        private void generateNormalFunctionCall(FunctionCall call) {
            for (Expression e : call.getArguments()) {
                visit(e);
            }
            String name = Type.getInternalName(AktkCompilerBuiltins.class);

            StringBuilder descriptor = new StringBuilder();
            descriptor.append("(");
            for (int i = 0; i<call.getArguments().size(); i++){
                descriptor.append("I");
            }
            descriptor.append(")I");
            mv.visitMethodInsn(INVOKESTATIC, name, call.getFunctionName(), descriptor.toString(), false);
        }

        private void generateArithmeticOrLogicOperation(FunctionCall call) {
            visit(call.getArguments().get(0));
            visit(call.getArguments().get(1));
            switch (call.getFunctionName()) {
                case "+":
                    mv.visitInsn(IADD);
                    break;
                case "-":
                    mv.visitInsn(ISUB);
                    break;
                case "*":
                    mv.visitInsn(IMUL);
                    break;
                case "/":
                    mv.visitInsn(IDIV);
                    break;
                case "%":
                    mv.visitInsn(IREM);
                    break;
                default:
                    throw new UnsupportedOperationException(call.getFunctionName());
            }
        }

        private void generateComparisonOperation(FunctionCall call) {
            // JVM int-ide võrdlusoperatsioonide valikus on kõik operatsioonid seotud jumpidega.
            // Kui sa ei taha avaldise väärtustamise koodi siduda if-lausega
            // siis ma kasuta jumpe lihtsalt selleks, et tekitada stacki tippu
            // kas 0 või 1
            Label _truelabel = new Label();
            Label _endif = new Label();
            visit(call.getArguments().get(0));
            visit(call.getArguments().get(1));
            switch (call.getFunctionName()) {
                case ">":
                    //IF_ICMPGT
                    mv.visitJumpInsn(IF_ICMPGT, _truelabel);
                    break;
                case "<":
                    //IF_ICMPLT
                    mv.visitJumpInsn(IF_ICMPLT, _truelabel);
                    break;
                case "!=":
                    //IF_ICMPNE
                    mv.visitJumpInsn(IF_ICMPNE, _truelabel);
                    break;
                case "==":
                    //IF_ICMPEQ
                    mv.visitJumpInsn(IF_ICMPEQ, _truelabel);
                    break;
                case ">=":
                    //IF_ICMPGE
                    mv.visitJumpInsn(IF_ICMPGE, _truelabel);
                    break;
                case "<=":
                    //IF_ICMPLE
                    mv.visitJumpInsn(IF_ICMPLE, _truelabel);
                    break;
                default:
                    throw new UnsupportedOperationException(call.getFunctionName());
            }
            mv.visitLdcInsn(0);
            mv.visitJumpInsn(GOTO, _endif);
            mv.visitLabel(_truelabel);
            mv.visitLdcInsn(1);
            mv.visitLabel(_endif);
        }

        @Override
        protected void visitVoid(FunctionDefinition functionDefinition) {
            throw new UnsupportedOperationException(); // pole vaja
        }

        @Override
        protected void visitVoid(IfStatement ifStatement) {
            Label doneLabel = new Label();
            Label elseLabel = new Label();

            visit(ifStatement.getCondition()); //CONDITION
            mv.visitJumpInsn(IFEQ, elseLabel); // if i == 0, then jump

            visit(ifStatement.getThenBranch()); //THEN
            mv.visitJumpInsn(GOTO, doneLabel);

            mv.visitLabel(elseLabel); //ELSE
            if (ifStatement.getElseBranch() != null) {
                visit(ifStatement.getElseBranch());
            }
            mv.visitLabel(doneLabel);
        }

        @Override
        protected void visitVoid(IntegerLiteral integerLiteral) {
            mv.visitLdcInsn(integerLiteral.getValue());
        }

        @Override
        protected void visitVoid(ReturnStatement returnStatement) {
            throw new UnsupportedOperationException(); // pole vaja
        }

        @Override
        protected void visitVoid(StringLiteral stringLiteral) {
            throw new UnsupportedOperationException(); // pole vaja
        }

        @Override
        protected void visitVoid(Variable variable) {
            int i = getVariableIndex(variable.getBinding());
            mv.visitVarInsn(ILOAD, i);
        }

        @Override
        protected void visitVoid(VariableDeclaration variableDeclaration) {
            if(variableDeclaration.getInitializer() != null) {
                visit(variableDeclaration.getInitializer());
                int i = getVariableIndex(variableDeclaration);
                mv.visitVarInsn(ISTORE, i);
            } else{
                int i = getVariableIndex(variableDeclaration);
                mv.visitLdcInsn(0);
                mv.visitVarInsn(ISTORE, i);
            }
        }

        @Override
        protected void visitVoid(WhileStatement whileStatement) {
            Label whileStartLabel = new Label();
            Label whileDoneLabel = new Label();

            mv.visitLabel(whileStartLabel);
            visit(whileStatement.getCondition()); //paneb stack tippu 1 kui TRUE; 0 kui FALSE.
            mv.visitJumpInsn(IFEQ, whileDoneLabel);

            visit(whileStatement.getBody());
            mv.visitJumpInsn(GOTO, whileStartLabel);

            mv.visitLabel(whileDoneLabel);
        }
    }
}