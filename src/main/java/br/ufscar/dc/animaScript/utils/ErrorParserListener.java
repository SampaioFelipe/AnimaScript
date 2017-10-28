package br.ufscar.dc.animaScript.utils;

import java.util.BitSet;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

public class ErrorParserListener implements ANTLRErrorListener {

    private ResultadoParser saida;

    public ErrorParserListener(ResultadoParser saida){
        this.saida = saida;
    }

    public void syntaxError(Recognizer<?, ?> recognizer, Object o, int i, int i1, String s, RecognitionException e) {
        if (!saida.isModificado()) {
            Token error_token = (Token) o;
            try {
                saida.printErro(i, "erro sintatico proximo a " + error_token.getText().replace("<EOF>", "EOF"));
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean b, BitSet bitSet, ATNConfigSet atnConfigSet) {
        if (!saida.isModificado()) {
            saida.printErro(i,"Ambiguidade [" + i + ":" + i1 + "]");
        }
    }

    public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitSet, ATNConfigSet atnConfigSet) {

    }

    public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atnConfigSet) {

    }
}