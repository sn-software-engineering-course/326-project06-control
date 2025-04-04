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
        int n = list.content.size();
        for (int i = 1; i < n - 1; i++)
            eval(list.content.get(i), env);
        return eval(list.content.get(n - 1), env);
    }

    private static LObject handleIF(LList list, Environment env) {
        final LObject cond = eval(list.content.get(1), env);
        return cond.toBoolean() ? eval(list.content.get(2), env) : eval(list.content.get(3), env);
    }

    private static LFunction handleFnStar(LList list, Environment env) {
        LList binds = (LList) list.content.get(1);
        LObject functionBody = list.content.get(2);
        return new LFunction(binds.content.size(), args -> {
            Environment newEnv = new Environment(env, binds, args);
            return eval(functionBody, newEnv);
        });
    }

    private static LObject eval(LObject obj, Environment env) {
        if (obj instanceof LInteger || obj instanceof LString || (obj instanceof LList && ((LList) obj).content.isEmpty())) {
            return obj;
        }
        if (obj instanceof LSymbol) {
            return env.lookup((LSymbol) obj);
        }
        if (obj instanceof LList) {
            LList list = (LList) obj;
            LObject first = list.content.get(0);
            if (first.equals(SYMBOL_LET) || first.equals(SYMBOL_LET_STAR)) {
                return handleLet(list, env, first.equals(SYMBOL_LET_STAR));
            }
            if (first.equals(SYMBOL_DO)) {
                return handleDo(list, env);
            }
            if (first.equals(SYMBOL_IF)) {
                return handleIF(list, env);
            }
            if (first.equals(SYMBOL_FN_STAR)) {
                return handleFnStar(list, env);
            }
            LObject operator = eval(first, env);
            if (!(operator instanceof LFunction)) {
                throw new RuntimeException("First element is not a function");
            }
            LFunction func = (LFunction) operator;
            ArrayList<LObject> evaluatedList = new ArrayList<>();
            evaluatedList.add(operator);
            for (int i = 1; i < list.content.size(); i++) {
                evaluatedList.add(eval(list.content.get(i), env));
            }
            return func.call(new LList(evaluatedList));
        }
        throw new RuntimeException("Unknown LObject type: " + obj);
    }

    public static LObject eval(LObject obj) {
        return eval(obj, GLOBALS);
    }
}