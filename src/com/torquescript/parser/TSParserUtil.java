package com.torquescript.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;

import static com.torquescript.parser.TSParser.*;
import static com.torquescript.psi.TSTypes.*;

public class TSParserUtil extends GeneratedParserUtilBase {
    /* ********************************************************** */
    // object_expr
    //     | assign_var_expr
    //     | assign_index_expr
    //     | assign_ref_expr
    //     | assign_ref_index_expr
    //     | call_global_expr
    //     | call_global_ns_expr
    //     | call_method_expr
    public static boolean parseExpressionStmtGroup(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "expr_stmt_group")) return false;
        boolean r;
        PsiBuilder.Marker m = enter_section_(b);
        r = object_expr(b, l + 1);
        if (!r) r = assign_var_expr(b, l + 1);
        if (!r) r = assign_index_expr(b, l + 1);
        if (!r) r = assign_ref_expr(b, l + 1);
        if (!r) r = assign_ref_index_expr(b, l + 1);
        if (!r) r = call_global_expr(b, l + 1);
        if (!r) r = call_global_ns_expr(b, l + 1);
        if (!r) r = call_method_expr(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // expr DOT ID assign_block
    private static boolean assign_ref_expr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "assign_ref_expr")) return false;
        boolean r;
        PsiBuilder.Marker m = enter_section_(b);
        r = expr(b, 0, -1);
        r = r && consumeTokensSmart(b, 0, DOT, ID);
        r = r && assign_block(b, l + 1);
        exit_section_(b, m, ASSIGN_REF_EXPR, r);
        return r;
    }

    // expr DOT ID BRACKET_OPEN array_index_expr BRACKET_CLOSE assign_block
    private static boolean assign_ref_index_expr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "assign_ref_index_expr")) return false;
        boolean r;
        PsiBuilder.Marker m = enter_section_(b);
        r = expr(b, 0, -1);
        r = r && consumeTokensSmart(b, 0, DOT, ID, BRACKET_OPEN);
        r = r && array_index_expr(b, l + 1);
        r = r && consumeToken(b, BRACKET_CLOSE);
        r = r && assign_block(b, l + 1);
        exit_section_(b, m, ASSIGN_REF_INDEX_EXPR, r);
        return r;
    }

    // expr expr DOT ID PAREN_OPEN arg_list? PAREN_CLOSE
    private static boolean call_method_expr(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "call_method_expr")) return false;
        boolean r;
        PsiBuilder.Marker m = enter_section_(b);
        r = expr(b, 0, 9);
        r = r && consumeTokensSmart(b, 0, DOT, ID, PAREN_OPEN);
        r = r && call_method_expr_3(b, l + 1);
        r = r && consumeToken(b, PAREN_CLOSE);
        exit_section_(b, m, CALL_METHOD_EXPR, r);
        return r;
    }

    // arg_list?
    private static boolean call_method_expr_3(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "call_method_expr_3")) return false;
        arg_list(b, l + 1);
        return true;
    }

}
