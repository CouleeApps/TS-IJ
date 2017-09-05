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
        if (!r) r = assign_ref_expr(b, l + 1);
        if (!r) r = assign_ref_index_expr(b, l + 1);
        if (!r) r = call_method_expr(b, l + 1);
        if (!r) r = assign_var_expr(b, l + 1);
        if (!r) r = assign_index_expr(b, l + 1);
        if (!r) r = call_global_expr(b, l + 1);
        if (!r) r = call_global_ns_expr(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    private static boolean assign_ref_expr(PsiBuilder b, int l) {
        boolean r;
        PsiBuilder.Marker m = enter_section_(b);
        r = expr(b, l + 1, -1);
        PsiBuilderImpl.ProductionMarker last = (PsiBuilderImpl.ProductionMarker) b.getLatestDoneMarker();
        r = last != null && r && last.getTokenType().equals(ASSIGN_REF_EXPR);
        exit_section_(b, m, null, r);
        return r;
    }

    private static boolean assign_ref_index_expr(PsiBuilder b, int l) {
        boolean r;
        PsiBuilder.Marker m = enter_section_(b);
        r = expr(b, l + 1, -1);
        PsiBuilderImpl.ProductionMarker last = (PsiBuilderImpl.ProductionMarker)b.getLatestDoneMarker();
        r = last != null && r && last.getTokenType().equals(ASSIGN_REF_INDEX_EXPR);
        exit_section_(b, m, null, r);
        return r;
    }

    private static boolean call_method_expr(PsiBuilder b, int l) {
        boolean r;
        PsiBuilder.Marker m = enter_section_(b);
        r = expr(b, l + 1, -1);
        PsiBuilderImpl.ProductionMarker last = (PsiBuilderImpl.ProductionMarker)b.getLatestDoneMarker();
        r = last != null && r && last.getTokenType().equals(CALL_METHOD_EXPR);
        exit_section_(b, m, null, r);
        return r;
    }
}
