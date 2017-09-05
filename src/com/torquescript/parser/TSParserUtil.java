package com.torquescript.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.impl.PsiBuilderImpl;
import com.intellij.lang.parser.GeneratedParserUtilBase;

import static com.torquescript.parser.TSParser.*;
import static com.torquescript.psi.TSTypes.*;

public class TSParserUtil extends GeneratedParserUtilBase {
    /* ********************************************************** */
    // object_expr
    //     | assign_var_expr
    //     | assign_index_expr
    //     | assign_ref_expr ***
    //     | assign_ref_index_expr ***
    //     | call_global_expr
    //     | call_global_ns_expr
    //     | call_method_expr ***
    public static boolean parseExpressionStmtGroup(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "expr_stmt_group")) return false;
        boolean r;
        PsiBuilder.Marker m = enter_section_(b);
        r = object_expr(b, l + 1);

        //Need to do these first since they match with expr
        if (!r) r = assign_ref_expr(b, l + 1);
        if (!r) r = assign_ref_index_expr(b, l + 1);
        if (!r) r = call_method_expr(b, l + 1);

        //If none of those wonky rules succeed, do the decent ones
        if (!r) r = assign_var_expr(b, l + 1);
        if (!r) r = assign_index_expr(b, l + 1);
        if (!r) r = call_global_expr(b, l + 1);
        if (!r) r = call_global_ns_expr(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    private static boolean assign_ref_expr(PsiBuilder b, int l) {
        //So this is rather wonky. What we do here is try to parse the incoming nodes as an expression,
        // as assign_ref_expr (and the below rules) need an expression as the first argument, but
        // you can chain them together. And calling expr will eat the assign_ref_expr when it parses.
        //So to get to the point here:
        // We run expr() and then see what it comes up with. If the last node (basically the expression
        // that it parsed from the call) is of our type, then we successfully parsed it.
        boolean r;
        PsiBuilder.Marker m = enter_section_(b);
        //Group -1 so we parse whatever expression is next
        r = expr(b, l + 1, -1);
        if (r) {
            //If we parsed an expression, check if we parsed the type we'ere looking for
            PsiBuilderImpl.ProductionMarker last = (PsiBuilderImpl.ProductionMarker) b.getLatestDoneMarker();
            r = last != null && last.getTokenType().equals(ASSIGN_REF_EXPR);
        }
        exit_section_(b, m, null, r);
        return r;
    }

    private static boolean assign_ref_index_expr(PsiBuilder b, int l) {
        //See assign_ref_expr for explanation
        boolean r;
        PsiBuilder.Marker m = enter_section_(b);
        r = expr(b, l + 1, -1);
        if (r) {
            PsiBuilderImpl.ProductionMarker last = (PsiBuilderImpl.ProductionMarker)b.getLatestDoneMarker();
            r = last != null && last.getTokenType().equals(ASSIGN_REF_INDEX_EXPR);
        }
        exit_section_(b, m, null, r);
        return r;
    }

    private static boolean call_method_expr(PsiBuilder b, int l) {
        //See assign_ref_expr for explanation
        boolean r;
        PsiBuilder.Marker m = enter_section_(b);
        r = expr(b, l + 1, -1);
        if (r) {
            PsiBuilderImpl.ProductionMarker last = (PsiBuilderImpl.ProductionMarker)b.getLatestDoneMarker();
            r = last != null && last.getTokenType().equals(CALL_METHOD_EXPR);
        }
        exit_section_(b, m, null, r);
        return r;
    }
}
