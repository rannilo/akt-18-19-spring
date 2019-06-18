grammar Wiki;
@header { package week8.eksam; }

wiki
    : pealkiri sektsioon
    ;

paragrahv
    : PARAGRAHV
    ;

pealkiri
    : TEKST KRIIPS
    ;
sektsioon
    :paragrahv
    ;
PEALKIRI
    : TEKST REAVAHETUS
    ;
PARAGRAHV
    : TEKST+
    ;
KRIIPS
    : '----' '-'+ REAVAHETUS
    ;
TEKST
    : [A-Za-z., ]+ REAVAHETUS
    ;
// Anname teile ette ühe reegli, millele saate viidata nii lekseri kui
// ka parseri reeglites. Asi selles, et Windows-is ja Linux-is/Mac-is
// kirjutatakse reavahetused erinevalt ja see võib tekitada jama, kui te
// tähistate oma reavahetust ainult '\n'-ga. See reegel tunneb
// ära reavahetuse nii Windows-is, Linux-is kui ka Mac-is.
REAVAHETUS
	:	('\r'?)'\n'
	;