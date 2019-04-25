grammar Quiz;
@header { package week8.eksam; }

quiz
	: yldinfo REAVAHETUS+ kysimused REAVAHETUS*
	| kysimused REAVAHETUS+ yldinfo REAVAHETUS*
	;

yldinfo
	: '--- YLDINFO ---' REAVAHETUS (pealkiri ajapiirang | ajapiirang pealkiri)
	;

pealkiri
	: 'pealkiri:' TEKST REAVAHETUS
	;

ajapiirang
	: 'ajapiirang: ' ARV ' min' REAVAHETUS
	;


kysimused
	:  '--- KYSIMUSED ---' REAVAHETUS (kysimus REAVAHETUS*)+
	;	

kysimus
	:	arvuvastusega
	|	lyhivastusega
	|	valikvastusega
	|	jahei
	|	essee
	;
	
essee
	:	'ESSEE:' TEKST REAVAHETUS
	;

jahei
	:	'JAH-EI:' TEKST REAVAHETUS 'oige: ' ('jah' | 'ei') REAVAHETUS
	;

valikvastusega
	:	'VALIKVASTUSEGA:' TEKST REAVAHETUS valeValik* oigeValik (valeValik | oigeValik)*
	;

oigeValik
	:	'+ ' TEKST REAVAHETUS	
	;

valeValik
	:	'- ' TEKST REAVAHETUS	
	;

lyhivastusega
	:	'LYHIVASTUSEGA:' TEKST REAVAHETUS 'oige: ' TEKST REAVAHETUS
	;

arvuvastusega
	:	'ARVUVASTUSEGA:' TEKST REAVAHETUS 'oige: ' ARV (' +/- ' ARV)? REAVAHETUS
	;

ARV
	:	[0-9]+
	;

TEKST
	:	[a-zA-Z .,?]+
	;
	
// Anname teile ette ühe reegli, millele saate viidata nii lekseri kui 
// ka parseri reeglites. Asi selles, et Windows-is ja Linux-is/Mac-is
// kirjutatakse reavahetused erinevalt ja see võib tekitada jama, kui te
// tähistate oma reavahetust ainult '\n'-ga. See reegel tunneb
// ära reavahetuse nii Windows-is, Linux-is kui ka Mac-is. 
REAVAHETUS
	:	('\r'?)'\n'
	;
