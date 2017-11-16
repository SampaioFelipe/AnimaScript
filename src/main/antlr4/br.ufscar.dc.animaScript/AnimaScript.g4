grammar AnimaScript;

@header {

}

@members{
static String grupo = "619523, 619744, 619930";
}

// Definicao de regras lexicas
fragment DIGITO: ('0'..'9');

fragment MINUSCULA: ('a'..'z');

fragment MAISCULA: ('A'..'Z');

IDENT : (MINUSCULA | '_') (MINUSCULA | MAISCULA | DIGITO| '_')*;
IDENT_ELEMENTO : MAISCULA (MINUSCULA | MAISCULA | DIGITO | '_')*;
ATRIBUTO_GLOBAL: '@'(MAISCULA | MINUSCULA) (MAISCULA | MINUSCULA | DIGITO| '_')*;
NUM_INT : DIGITO+;
NUM_REAL : DIGITO+ '.' DIGITO+;

OP: ('+'|'-'|'*'|'/');

CADEIA : '"' ~('\n' | '\r' | '"')* '"';
VAL_LOGICO : 'true' | 'false';
TEMPO: ((DIGITO+ 'h')? DIGITO+'m')? DIGITO+ 's';
//COR_HEX: (DIGITO | ('a'..'f') | ('A'..'F'))*;

COMENTARIO : '#' ~('\n')* -> skip;
WS	:	(' ' | '\t' | '\r' | '\n') -> skip;

// Caso haja qualquer caracter que nao pertenca a gramatica, e indentificado como um erro lexico
//ERROR: . {Main.lexicalError = "Linha "+getLine()+": "+getText()+" - simbolo nao identificado";};
// O mesmo acontece caso o haja algum comentario nao fechado

programa: decl_global composition elements scene storyboard;

decl_global: (ATRIBUTO_GLOBAL valor)*;

valor: CADEIA | TEMPO | expressao;

expressao: expressao2;

expressao2: ((NUM_INT | NUM_REAL) (OP (NUM_INT | NUM_REAL))* unidade?)(OP expressao)* | ('(' expressao')');

unidade: '%' | 'deg' | 'rad' | 'f';

composition: 'composition' ':' (decls+=decl_prop)+;

decl_prop: prop=IDENT '=' (valor | IDENT);

elements: 'elements' ':' decl_element+;

decl_element: IDENT_ELEMENTO '{' (decl_prop | decl_action | element)* '}';

decl_action: 'action' IDENT '(' (IDENT (',' IDENT)*)? ')' '{' comando* '}';

action: IDENT '(' (expressao (',' expressao)*)? ')';

element: IDENT_ELEMENTO IDENT (',' IDENT)*;

atributo_element: IDENT '.' IDENT;

op_atribuicao: '=' | '+=' | '-='| '*=';

scene: 'scene' ':' (element)+;

storyboard:'storyboard' ':' keyframe*;

keyframe: '['expressao']' ':' comando+;

comando: IDENT '.' action '.' ('start' | 'stop');