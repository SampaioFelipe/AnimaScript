grammar AnimaScript;

@members{
static String grupo = "619523, 619744, 619930";
}

// Definicao de regras lexicas
fragment DIGIT: ('0'..'9');

fragment LOWER_CASE: ('a'..'z');

fragment UPPER_CASE: ('A'..'Z');


OP_MATH: ('+'|'-'|'*'|'/');

OP_ATTRIB: '=';
OP_ATTRIB2: '+=' | '-='| '*=';

STRING : '"' ~('\n' | '\r' | '"')* '"';
LOGIC_VAL : 'true' | 'false';

NUM_INT : DIGIT+;
NUM_REAL : DIGIT+ '.' DIGIT+;

HOUR_FORMAT: (((DIGIT? DIGIT 'h')? DIGIT? DIGIT 'm')? DIGIT? DIGIT 's')? DIGIT? DIGIT 'f';

IDENT : (LOWER_CASE | '_') (LOWER_CASE | UPPER_CASE | DIGIT | '_')*;
IDENT_DECL_ELEMENT : UPPER_CASE (LOWER_CASE | UPPER_CASE | DIGIT | '_')*;
GLOBAL_ATTR: '@'(LOWER_CASE) (UPPER_CASE | LOWER_CASE | DIGIT| '_')*;

COMMENT : '#' ~('\n')* -> skip;
WHITE_SPACE :	(' ' | '\t' | '\r' | '\n') -> skip;

// Caso haja qualquer caracter que nao pertenca a gramatica, e indentificado como um erro lexico
ERROR: . {Main.out.printErro(getLine(), getText() + "- simbolo nao identificado");};

program
    : decl_global composition elements scene storyboard;

decl_global
    : (GLOBAL_ATTR value)*;

value
    : STRING | time | expr;

time
    : HOUR_FORMAT;

expr
    :'-'?(NUM_INT | NUM_REAL | attr) (OP_MATH expr)?
    |('(' expr')');

composition
    :'composition' ':' (attrs+=decl_attr_comp)+;

decl_attr
    :attr (OP_ATTRIB|OP_ATTRIB2) value;

decl_attr_comp
    :attr OP_ATTRIB value;

attr
    :idents+=IDENT ('.' idents+=IDENT)*;

elements
    :'elements' ':' (decls+=decl_element)+;

decl_element
    :IDENT_DECL_ELEMENT '{' (decl_attr | decl_action | element_instance)* '}';

decl_action
    :'action' name=IDENT '(' (params+=IDENT (',' params+=IDENT)*)? ')' '{' command* '}';

action_call
    :'start' attr'(' (params+=expr (',' params+=expr)*)? ')'
    |'stop' attr;

element_instance
    :IDENT_DECL_ELEMENT name=IDENT '('(params+=expr(',' params+=expr)*)?')';

scene
    :'scene' ':' (element_instance)+;

storyboard
    :'storyboard' ':' keyframe*;

keyframe
    :'['time']' ':' cmds+=command+;

command
    : decl_attr
    | action_call;