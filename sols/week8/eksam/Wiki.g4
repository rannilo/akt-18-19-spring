grammar Wiki;
@header { package week8.eksam; }

wiki
    : pealkiri sektsioon (REAVAHETUS sektsioon)*
    ;

pealkiri
	: 	RIDA KRIIPS
	;
	
sektsioon
	:	paragrahv
	|	loetelu
	|	kood
	;

paragrahv
	:	RIDA+
	;

loetelu
	:	loetelu1Punkt+
	;


kood
	:	koodiRida+
	;
loetelu1Punkt
	:	'* ' RIDA (loetelu2Punkt)*
	; 	 	

loetelu2Punkt
	:	'** ' RIDA (loetelu3Punkt)*
	; 	 	

loetelu3Punkt
	:	'*** ' RIDA
	;


koodiRida
	:	'> ' RIDA
	;
	


RIDA
	:	[a-zA-Z .,]+ REAVAHETUS
	;


KRIIPS
	:	'-----' '-'* REAVAHETUS
	;


// Anname teile ette ühe reegli, millele saate viidata nii lekseri kui 
// ka parseri reeglites. Asi selles, et Windows-is ja Linux-is/Mac-is
// kirjutatakse reavahetused erinevalt ja see võib tekitada jama, kui te
// tähistate oma reavahetust ainult '\n'-ga. See reegel tunneb
// ära reavahetuse nii Windows-is, Linux-is kui ka Mac-is. 
REAVAHETUS
	:	('\r'?)'\n'
	;
