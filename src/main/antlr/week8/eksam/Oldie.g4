grammar Oldie;
@header { package week8.eksam; }

// Ära seda reeglit muuda, selle kaudu testitakse grammatikat 
programm
	:	programmiSisu EOF
	;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
programmiSisu
	: // Tee lõpuni...
	;

// Anname teile ette ühe reegli, millele saate viidata nii lekseri kui 
// ka parseri reeglites. Asi selles, et Windows-is ja Linux-is/Mac-is
// kirjutatakse reavahetused erinevalt ja see võib tekitada jama, kui te
// tähistate oma reavahetust ainult '\n'-ga. See reegel tunneb
// ära reavahetuse nii Windows-is, Linux-is kui ka Mac-is. 
REAVAHETUS
	:	('\r'?)'\n'
	;
