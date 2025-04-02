package edu.nju.software.eval;

import edu.nju.software.ast.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Evaluator {
    private static final Environment GLOBALS = new Environment();
    private static final LSymbol SYMBOL_LET = new LSymbol("let");
    private static final LSymbol SYMBOL_LET_STAR = new LSymbol("let*");

    private static final LSymbol SYMBOL_DO = new LSymbol("do");
    private static final LSymbol SYMBOL_IF = new LSymbol("if");
    private static final LSymbol SYMBOL_FN_STAR = new LSymbol("fn*");

    static {
        GLOBALS.define(new LSymbol("+"), new LFunction(2,
                list -> new LInteger(((LInteger) list.content.get(1)).value + ((LInteger) list.content.get(2)).value)));
        GLOBALS.define(new LSymbol("-"), new LFunction(2,
                list -> new LInteger(((LInteger) list.content.get(1)).value - ((LInteger) list.content.get(2)).value)));
        GLOBALS.define(new LSymbol("*"), new LFunction(2,
                list -> new LInteger(((LInteger) list.content.get(1)).value * ((LInteger) list.content.get(2)).value)));
        GLOBALS.define(new LSymbol("/"), new LFunction(2,
                list -> new LInteger(((LInteger) list.content.get(1)).value / ((LInteger) list.content.get(2)).value)));
        GLOBALS.define(new LSymbol("nil"), new LList());
        GLOBALS.define(new LSymbol("cons"), new LFunction(2, list -> {
            LObject toAdd = list.content.get(1);
            ArrayList<LObject> newList = new ArrayList<>();
            newList.add(toAdd);
            newList.addAll(((LList) list.content.get(2)).content);
            return new LList(newList);
        }));
    }

    private static LObject handleLet(LList list, Environment env, boolean inplace) {
        final LList bindings = (LList) list.content.get(1);
        final Environment newEnv = new Environment(env);
        for (LObject bindingObj : bindings.content) {
            LList binding = (LList) bindingObj;
            newEnv.define((LSymbol) binding.content.get(0), eval(binding.content.get(1), inplace ? newEnv : env));
        }
        return eval(list.content.get(2), newEnv);
    }

    private static LObject handleDo(LList list, Environment env) {
        // TODO: YOUR CODE HERE
        return null;
    }

    private static LObject handleIF(LList list, Environment env) {
        // TODO: YOUR CODE HERE
        return null;
    }

    private static LFunction handleFnStar(LList list, Environment env) {
        // TODO: YOUR CODE HERE
        return null;
    }

    private static LObject eval(LObject obj, Environment env) {
        // TODO: YOUR CODE HERE
        return null;
    }

    public static LObject eval(LObject obj) {
        return eval(obj, GLOBALS);
    }
}