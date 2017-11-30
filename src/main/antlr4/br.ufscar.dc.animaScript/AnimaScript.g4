grammar AnimaScript;

@header {

}

@members{
static String grupo = "619523, 619744, 619930";
}

// Definicao de regras lexicas
fragment DIGIT: ('0'..'9');

fragment LOWER_CASE: ('a'..'z');

fragment UPPER_CASE: ('A'..'Z');

UNIT: '%' | 'deg' | 'rad';

OP_MATH: ('+'|'-'|'*'|'/');

OP_ATTRIB: '=';
OP_ATTRIB2: '+=' | '-='| '*=';

STRING : '"' ~('\n' | '\r' | '"')* '"';
LOGIC_VAL : 'true' | 'false';

OP_ACTION: 'start'|'stop';

NUM_INT : DIGIT+;
NUM_REAL : DIGIT+ '.' DIGIT+;

HOUR_FORMAT: ((DIGIT? DIGIT 'h')? DIGIT? DIGIT 'm')? DIGIT? DIGIT 's';

IDENT : (LOWER_CASE | '_') (LOWER_CASE | UPPER_CASE | DIGIT | '_')*;
IDENT_DECL_ELEMENT : UPPER_CASE (LOWER_CASE | UPPER_CASE | DIGIT | '_')*;
GLOBAL_ATTR: '@'(LOWER_CASE) (UPPER_CASE | LOWER_CASE | DIGIT| '_')*;

COMMENT : '#' ~('\n')* -> skip;
WHITE_SPACE :	(' ' | '\t' | '\r' | '\n') -> skip;

// Caso haja qualquer caracter que nao pertenca a gramatica, e indentificado como um erro lexico
//ERROR: . {Main.lexicalError = "Linha "+getLine()+": "+getText()+" - simbolo nao identificado";};
// O mesmo acontece caso o haja algum COMMENT nao fechado

program
    : decl_global composition elements scene storyboard;

decl_global
    : (GLOBAL_ATTR value)*;

value
    : STRING | time | num_value | position;

num_value
    : (expr | IDENT) UNIT?;

position
    : '(' num_value ',' num_value ')';

time
    : NUM_INT 'f'
    | HOUR_FORMAT;

expr
    : ((NUM_INT | NUM_REAL) (OP_MATH (NUM_INT | NUM_REAL))*)(OP_MATH expr)*
    | ('(' expr')');

composition
    : 'composition' ':' (attrs+=decl_attr_comp)+;

decl_attr
    : attr (OP_ATTRIB|OP_ATTRIB2) value;

decl_attr_comp
    : attr OP_ATTRIB value;

attr: IDENT ('.' IDENT)*;

elements: 'elements' ':' (decls+=decl_element)+;

decl_element: IDENT_DECL_ELEMENT '{' (decl_attr | decl_action | element_instance)* '}';

decl_action: 'action' name=IDENT params '{' command* '}';

params: '(' (IDENT (',' IDENT)*)? ')';

action_call: OP_ACTION attr'(' (expr (',' expr)*)? ')';

element_instance: IDENT_DECL_ELEMENT idents+=IDENT (',' idents+=IDENT)*;

scene: 'scene' ':' (element_instance)+;

storyboard:'storyboard' ':' keyframe*;

keyframe: '['time']' ':' cmds+=command+;

command: decl_attr | action_call;