/**
 * @author Vitor Schweitzer e Caio Cardoso
 * created on 2016/06/18
 */
package br.ufsc.ctc.ine.sin.ine5622.model;

public interface ParserConstants {
    int START_SYMBOL = 58;

    int FIRST_NON_TERMINAL    = 58;
    int FIRST_SEMANTIC_ACTION = 92;

    int[][] PARSER_TABLE = {
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  1, -1, -1, -1, -1,  1,  1, -1, -1, -1,  1,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  1, -1, -1,  1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  3, -1, -1, -1, -1,  2,  2, -1, -1, -1,  2,  2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  2, -1, -1,  3, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  7,  8, -1, -1, -1,  4,  6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  5, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, 13, 12, -1, -1, -1, 13, -1, 13, -1, -1, -1, -1, -1, -1, 13, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, 15, -1, -1, -1, -1, 14, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 16, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 18, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, 20, -1, -1, -1, -1, -1, 19, -1, -1, -1, -1, -1, -1, -1, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, 21, -1, -1, -1, -1, -1, -1, 22, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, 24, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 23, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 25, 26, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 27, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, 28, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 29, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 30, -1, -1, -1, 37, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 31, 37, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 32, -1, 37, 34, 35, -1, -1, -1, -1, -1, -1, -1, -1, -1, 33, -1, -1, -1, 36, -1 },
        { -1, -1, -1, -1, -1, 39, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 39, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 38, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, 43, -1, -1, -1, -1, -1, 42, -1, 41, -1, -1, -1, -1, -1, -1, 40, -1, -1, -1, -1, 43, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 43, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 44, 44, 44, 44, -1, -1, -1, -1, -1, -1, 44, -1, -1, -1, -1, 44, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 44, 44, 44, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, 45, -1, -1, -1, -1, -1, 46, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 47, 47, 47, 47, -1, -1, -1, -1, -1, -1, 47, -1, -1, -1, -1, 47, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 47, 47, 47, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, 49, 49, -1, 48, 48, 48, -1, 49, -1, 49, -1, -1, -1, -1, -1, -1, -1, 48, 48, -1, 49, 48, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 49, 49, -1, -1, -1, -1, -1, -1, -1, -1, 49, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, 52, 51, 50, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 55, 54, -1, -1, 53, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 56, 56, 56, 56, -1, -1, -1, -1, -1, -1, 56, -1, -1, -1, -1, 56, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 56, 56, 56, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, 58, 58, -1, 58, 58, 58, -1, 58, -1, 58, 57, 57, -1, -1, -1, -1, -1, 58, 58, -1, 58, 58, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 58, 58, -1, -1, 57, -1, -1, -1, -1, -1, 58, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 59, 60, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 61, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 62, 62, 62, 62, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, -1, 62, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, 62, 62, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, 64, 64, -1, 64, 64, 64, -1, 64, -1, 64, 64, 64, 63, 63, -1, -1, -1, 64, 64, -1, 64, 64, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 64, 64, -1, -1, 64, 63, -1, -1, -1, -1, 64, -1, -1, -1, -1, -1, -1, -1, 63 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 65, 66, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 67, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 68 },
        { -1, 72, 73, 73, 73, -1, -1, -1, -1, -1, -1, 71, -1, -1, -1, -1, 70, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 69, 73, 73, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, 76, 76, -1, 76, 76, 76, 74, 76, 75, 76, 76, 76, 76, 76, -1, -1, -1, 76, 76, -1, 76, 76, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 76, 76, -1, -1, 76, 76, -1, -1, -1, -1, 76, -1, -1, -1, -1, -1, -1, -1, 76 },
        { -1, 77, 78, 78, 78, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 78, 78, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, 79, 80, 83, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 81, 82, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }
    };

    int[][] PRODUCTIONS = {
        {  28,   2, 193,   6,  59,   8 },
        {  60,  66,  72 },
        {  61,  62, 194,  63, 195,  65, 196,   6,  60 },
        {   0 },
        {  35, 197 },
        {  50, 198 },
        {  36, 199 },
        {  30, 200 },
        {  31,  14,  90, 201,  15 },
        { 202,  14,  90, 203,  15 },
        { 204 },
        {   2, 205,  64 },
        {   7,  63 },
        {   0 },
        { 206,  11,  90, 207 },
        { 208 },
        {  67,   6,  66 },
        {   0 },
        {  53,   2, 209,  68, 210,  70, 211,   6,  59, 212 },
        {  12,  71, 213,  63, 214,  20,  61, 215,  69,  13 },
        {   0 },
        {   6,  71, 213,  63, 214,  20,  61, 215,  69 },
        {   0 },
        {  20,  61, 216 },
        { 217 },
        {  54, 218 },
        {  55, 219 },
        {  25,  74,  73,  26 },
        {   6,  74,  73 },
        {   0 },
        {   2, 220,  76 },
        {  72 },
        {  38,  79, 221,  39,  74,  75 },
        {  52,  79, 221,  49,  74 },
        {  41,  12, 222,  63,  13 },
        {  42,  12, 223,  77,  13 },
        {  56,  79, 224 },
        {   0 },
        {  40,  74 },
        {   0 },
        { 225,  21,  79, 226 },
        {  14, 227,  79, 228,  15,  21,  79, 226 },
        {  12, 229, 230,  77,  13, 231 },
        { 232 },
        {  79, 233,  78 },
        {   7,  79, 233,  78 },
        {   0 },
        {  82, 234,  80 },
        {  81,  82, 235 },
        {   0 },
        {  11, 236 },
        {  10, 237 },
        {   9, 238 },
        {  27, 239 },
        {  24, 240 },
        {  23, 241 },
        {  85, 242,  83 },
        {  84, 243,  85, 244,  83 },
        {   0 },
        {  16, 245 },
        {  17, 246 },
        {  43, 247 },
        {  88, 248,  86 },
        {  87, 249,  88, 250,  86 },
        {   0 },
        {  18, 251 },
        {  19, 252 },
        {  44, 253 },
        {  57, 254 },
        {  45, 255,  88, 256 },
        {  17, 257,  88, 258 },
        {  12, 259,  79,  13, 260 },
        {   2, 220,  89, 261 },
        {  91, 262 },
        {  12, 263,  79, 233,  78,  13, 264 },
        {  14, 227,  79, 265,  15 },
        { 266 },
        {   2, 267 },
        {  91 },
        {   3, 268 },
        {   4, 269 },
        {  46, 270 },
        {  47, 271 },
        {   5, 272 }
    };

    String[] PARSER_ERROR =
    {
        "",
        "Era esperado fim de programa",
        "Era esperado id",
        "Era esperado num_int",
        "Era esperado num_real",
        "Era esperado literal",
        "Era esperado \";\"",
        "Era esperado \",\"",
        "Era esperado \".\"",
        "Era esperado \">\"",
        "Era esperado \"<\"",
        "Era esperado \"=\"",
        "Era esperado \"(\"",
        "Era esperado \")\"",
        "Era esperado \"[\"",
        "Era esperado \"]\"",
        "Era esperado \"+\"",
        "Era esperado \"-\"",
        "Era esperado \"*\"",
        "Era esperado \"/\"",
        "Era esperado \":\"",
        "Era esperado \":=\"",
        "Era esperado \"..\"",
        "Era esperado \"<>\"",
        "Era esperado \"<=\"",
        "Era esperado \"{\"",
        "Era esperado \"}\"",
        "Era esperado \">=\"",
        "Era esperado programa",
        "Era esperado var",
        "Era esperado caracter",
        "Era esperado cadeia",
        "Era esperado procedimento",
        "Era esperado inicio",
        "Era esperado fim",
        "Era esperado inteiro",
        "Era esperado booleano",
        "Era esperado funcao",
        "Era esperado se",
        "Era esperado entao",
        "Era esperado senao",
        "Era esperado leia",
        "Era esperado escreva",
        "Era esperado ou",
        "Era esperado e",
        "Era esperado nao",
        "Era esperado falso",
        "Era esperado verdadeiro",
        "Era esperado de",
        "Era esperado faca",
        "Era esperado real",
        "Era esperado vetor",
        "Era esperado enquanto",
        "Era esperado metodo",
        "Era esperado ref",
        "Era esperado val",
        "Era esperado retorne",
        "Era esperado div",
        "Era esperado programa ",// <programa>
        "Era esperado inteiro, real, booleano, caracter ou cadeia ",// <bloco>
        "Era esperado inteiro, real, booleano, caracter ou cadeia ",// <dcl_var_const>
        "Era esperado inteiro, real, booleano, caracter ou cadeia ",// <<tipo>
        "Era esperado \"[\"",// <dimensao>
        "Era esperado id",// <lid>",
        "Era esperado \",\"",// <rep_lid>
        "Era esperado \"=\"", // <fator_const>
        "Era esperado metodo", // <dcl_metodos>
        "Era esperado metodo", // <dcl_metodo>
        "Era esperado \"(\"", // <par_formais>
        "Era esperado \";\"", // <rep_par>
        "Era esperado \":\"", // <tipo_metodo>
        "Era esperado ref ou val", //<mp_par>
        "Era esperado \"{\"", // <com_composto>
        "Era esperado \";\"", // <replistacomando>
        "Era esperado id, se, enquanto, leia, escreva, retorne ou \";\"", // <comando>
        "Era esperado senao", // <senaoparte>
        "Era esperado \":=\", \"[\", ou \"(\"", // <rcomid>
        "Era esperado nao, id, num_int, num_real, falso, verdadeiro, literal, \"-\" ou \"(\"", // <lista_expr>
        "Era esperado \",\"", // <rep_lexpr>
        "Era esperado nao, id, num_int, num_real, falso, verdadeiro, literal, \"-\" ou \"(\"", // <expressao>
        "Era esperado \"=\", \"<\", ou \">\"", // <resto_expressao> inválido",
        "Era esperado \"=\", \"<\", ou \">\"", // <oprel>
        "Era esperado nao, id, num_int, num_real, falso, verdadeiro, literal, \"-\" ou \"(\"", // <expsimp>
        "Era esperado ou, \"+\", \"-\"", // <rep_expsimp>
        "Era esperado ou, \"+\", \"-\"", // <op_add>
        "Era esperado nao, id, num_int, num_real, falso, verdadeiro, literal, \"-\" ou \"(\"", // <termo>
        "Era esperado div, e, \"*\" ou \"/\"", // <rep_termo> inválido",
        "Era esperado div, e, \"*\" ou \"/\"", // <op_mult> inválido",
        "Era esperado nao, id, num_int, num_real, falso, verdadeiro, literal, \"-\" ou \"(\"", // <fator>
        "Era esperado \"[\" ou \"(\"", // <rvar> inválido",
        "Era esperado id, num_int, num_real, falso, verdadeiro ou literal", // <constante> inválido",
        "Era esperado num_int, num_real, falso, verdadeiro ou literal", // <constante_explicita> inválido"
    };
}
