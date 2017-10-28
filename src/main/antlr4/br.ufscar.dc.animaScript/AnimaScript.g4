grammar AnimaScript;

@header {

}

@members{
static String grupo = "619523, 619744, 619930";
}

// Definicao de regras lexicas
fragment DIGITO: ('0'..'9');

fragment LETRA: ('a'..'z'|'A'..'Z');

IDENT : (LETRA | '_') (LETRA | DIGITO| '_')*;
ATRIBUTO_GLOBAL: '@'LETRA (LETRA | DIGITO| '_')*;
NUM_INT : DIGITO+;
NUM_REAL : DIGITO+ '.' DIGITO+;

CADEIA : '"' ~('\n' | '\r' | '"')* '"';
VAL_LOGICO : 'true' | 'false';
TEMPO: ((DIGITO+ 'h')? DIGITO+'m')? DIGITO+ 's';
COR_HEX: '#'(DIGITO | ('a'..'f') | ('A'..'F'))*;

COMENTARIO : '--' ~('\n')* -> skip;
WS	:	(' ' | '\t' | '\r' | '\n') -> skip;

// Caso haja qualquer caracter que nao pertenca a gramatica, e indentificado como um erro lexico
//ERROR: . {Main.lexicalError = "Linha "+getLine()+": "+getText()+" - simbolo nao identificado";};
// O mesmo acontece caso o haja algum comentario nao fechado

programa: decl_global composition elements storyboard;

decl_global: (ATRIBUTO_GLOBAL valor)*;

valor: CADEIA | TEMPO | expressao;

expressao: (NUM_INT | NUM_REAL) unidade?;

unidade: '%' | 'deg' | 'rad' | 'f';

composition: 'composition' ':' decl_prop+;

decl_prop: IDENT '=' (valor | IDENT);

elements: 'elements' ':' decl_element+;

decl_element: IDENT '{' (decl_prop | decl_action | element)* '}';

decl_action: 'action' IDENT '(' (IDENT (',' IDENT)*)? ')' '{' comando* '}';

action: IDENT '(' (expressao (',' expressao)*)? ')';

element: IDENT IDENT;

atributo_element: IDENT '.' IDENT;

comando: (IDENT | atributo_element) op_atribuicao (IDENT | atributo_element | expressao)
       | (IDENT '.')?action;

op_atribuicao: '=' | '+=' | '-='| '*=';

storyboard:'storyboard' ':' keyframe*;

keyframe: '['expressao']' ':' comando+;