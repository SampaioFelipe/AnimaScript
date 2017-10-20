grammar AnimaScript;

@header {

}

@members{
static String grupo = "619523, 619744, 619930";
}


// Definicao de regras lexicas
IDENT : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;
NUM_INT : ('0'..'9')+;
NUM_REAL : ('0'..'9')+'.'('0'..'9')+;
CADEIA : '"' ~('\n' | '\r' | '"')* '"';

COMENTARIO :	'{' ~('}')* '}' -> skip;
WS	:	(' ' | '\t' | '\r' | '\n') -> skip;

// Caso haja qualquer caracter que nao pertenca a gramatica, e indentificado como um erro lexico
//ERROR: . {Main.lexicalError = "Linha "+getLine()+": "+getText()+" - simbolo nao identificado";};
// O mesmo acontece caso o haja algum comentario nao fechado
//COMENTARIO_NAO_FECHADO: '{' ~('}' | '\n')* '\n' {Main.lexicalError = "Linha "+getLine()+ ": comentario nao fechado";};

programa:;