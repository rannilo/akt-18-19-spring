grammar Quiz;
@header { package week8.eksam; }

quiz: yldinfo kysimused;

yldinfo: '--- YLDINFO ---' RIDA (pealkiri RIDA ajapiirang | ajapiirang RIDA pealkiri) RIDA+;
kysimused: '--- KYSIMUSED ---' RIDA (kysimus RIDA+)+;
kysimus: arvuvastusega | lyhivastusega | valikvastusega | jahei | essee;
arvuvastusega: 'ARVUVASTUSEGA: ' TEKST RIDA 'oige: ' ARV (' +/- ' ARV)?;
lyhivastusega: 'LYHIVASTUSEGA: ' TEKST RIDA 'oige: ' TEKST;
valikvastusega: 'VALIKVASTUSEGA: ' TEKST valikuloetelu;
valikuloetelu: (RIDA '- ' TEKST)* (RIDA '+' TEKST RIDA)+ (RIDA '-' TEKST| RIDA '+' TEKST)*;
jahei: 'JAH-EI: ' TEKST RIDA 'oige: ' ('jah' | 'ei');
essee: 'ESSEE: ' TEKST;
pealkiri: 'pealkiri: ' TEKST;
ajapiirang: 'ajapiirang: ' ARV ' min';
TEKST: [a-zA-Z.,!? ]+;
ARV: [0-9]+;


// Anname teile ette ühe reegli, millele saate viidata nii lekseri kui
// ka parseri reeglites. Asi selles, et Windows-is ja Linux-is/Mac-is
// kirjutatakse reavahetused erinevalt ja see võib tekitada jama, kui te
// tähistate oma reavahetust ainult '\n'-ga. See reegel tunneb
// ära reavahetuse nii Windows-is, Linux-is kui ka Mac-is.
RIDA
	:	('\r'?)'\n'
	;
