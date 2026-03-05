/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author tempus
 */
package com.SimpleCalc;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculate {
    private static readWrite rw = new readWrite();
    private static boolean isRad = false;
    private static double pi = Math.PI;
    private static double el = Math.E;
    public Calculate() {
    }
    public boolean isRad(){
        return isRad;
    } 
    public void changeScaleToRad(boolean t){
        try{
            if (t){
                isRad = true;
            }
            else{
                isRad = false; 
            }
        }
        catch(Exception e){
            rw.writeToFile(">>>> Error in changeScaleToRad\n" + e + "\n", "log", true);
        }
    }
    public String replaceSymbolsWithNumbers(String exp){
        String modified = exp;
        String modified2 = modified;
        try{
            Pattern pattern = Pattern.compile("e+");
            Matcher matcher = pattern.matcher(modified2);
            if(matcher.find()) {
                 modified = modified2.replaceAll("e+", "e^" + matcher.group().length());
            }
            else {
                modified = modified2;
            }
            modified2 = modified;
            pattern = Pattern.compile("(?<=n\\(e\\^)(\\d+)(?=\\))");
            matcher = pattern.matcher(modified2);
            if(matcher.find()) {
                 modified = modified2.replaceAll("n\\(e\\^\\d+\\)", matcher.group());
            }
            else {
                modified = modified2;
            }
            modified2 = modified;
            pattern = Pattern.compile("Π+");
            matcher = pattern.matcher(modified2);
            if(matcher.find()) {
                 modified = modified2.replaceAll("Π+", "Π^" + matcher.group().length());
            }
            else {
                modified = modified2;
            }
        }
        catch(Exception e){
            rw.writeToFile(">>>> Error in replaceSymbolsWithNumbers\n" + e + "\n", "log", true);
        }
        return modified;
    }
    public String replaceFunction(String exp){
        String modified = exp;
        String modified2 = modified;
        try{
            modified2 = modified.replaceAll("arcsin","m");
            modified = modified2;
            modified2 = modified.replaceAll("arccos","q");
            modified = modified2;
            modified2 = modified.replaceAll("arctg","r");
            modified = modified2;
            modified2 = modified.replaceAll("sin","s");
            modified = modified2;
            modified2 = modified.replaceAll("cos","c");
            modified = modified2;
            modified2 = modified.replaceAll("tg","t");
            modified = modified2;
            modified2 = modified.replaceAll("lg","g");
            modified = modified2;
            modified2 = modified.replaceAll("ln","n");
        }
        catch(Exception e){
            rw.writeToFile(">>>> Error in replaceFunction\n" + e + "\n", "log", true);
        }
        return modified2;
    }
    public String dealWithSoleMinus(String exp){
        String modified = "";
        String modified1 = "";
        try{
            if (exp.toCharArray()[0] == '-'){
                modified = "0" + exp;
            }
            else{
                modified = exp;
            }
            modified1 = modified.replaceAll("\\(\\s*-", "(0-");
        }
        catch(Exception e){
            rw.writeToFile(">>>> Error in dealWithSoleMinus\n" + e + "\n", "log", true);
        }
        return modified1;
    }
    public boolean syntaxIsCorrect(String syntax){
        try{
            Pattern pattern1 = Pattern.compile("\\b[+-/*%.]{2,}\\b", Pattern.CASE_INSENSITIVE);
            Pattern pattern2 = Pattern.compile("\\)[+-/.*%.]+\\(", Pattern.CASE_INSENSITIVE);
            Matcher matcher1 = pattern1.matcher(syntax);
            Matcher matcher2 = pattern2.matcher(syntax);
            boolean matchFound1 = matcher1.find();
            boolean matchFound2 = matcher2.find();        
            if(matchFound1 || matchFound2 || (countBrackets("[(]+",syntax)!=countBrackets("[)]+",syntax))) {
               return false;
            }
        }
        catch(Exception e){
            rw.writeToFile(">>>> Error in syntaxIsCorrect\n" + e + "\n", "log", true);
        }
        return true;
    }
    private int countBrackets(String compile,String exp){
        Pattern pattern = Pattern.compile(compile);
        Matcher matcher = pattern.matcher(exp);
        int count = 0;
        try{
            while (matcher.find()) {
                count++;
            }
        }
        catch(Exception e){
            rw.writeToFile(">>>> Error in countBrackets\n" + e + "\n", "log", true);
        }
        return count;
    }
    public String resolveEquation(String equation) {
        char[] tokens = equation.toCharArray();
        Stack<String> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ') {
                continue;
           }
           if ((tokens[i] >= '0' && tokens[i] <= '9') || tokens[i] == '.') {
               try{
                    StringBuilder sb = new StringBuilder();
                    while (i < tokens.length && (Character.isDigit(tokens[i]) || tokens[i] == '.' )) {
                        sb.append(tokens[i]);
                        i++;
                    }
                    values.push(sb.toString());
                    i--;
                }
                catch(Exception e){
                    rw.writeToFile(">>>> Error in constructing numbers\n" + e + "\n", "log", true);
                }
            }
            else if (tokens[i] == '%') {
                // If the character is '%'
                try{
                    // Verify if there is +,-,* or / operator in stack and if the stack with operators is not empty
                    if(!operators.isEmpty() && (operators.peek() == '+' || operators.peek() == '-'
                        || operators.peek() == '*' || operators.peek() == '/')){
                            values.push(applyProcent(operators.pop(),
                            values.pop(),
                            values.pop()));
                    }
                    // If no other operators exist before value,exeption to this is ( 
                    else {
                        values.push(Double.toString(Double.parseDouble(values.pop()) / 100.0));
                    }
                }
                catch(Exception e){
                    rw.writeToFile(">>>> Error in processing %\n" + e + "\n", "log", true);
                }
            }else if(tokens[i] == 'e'
                    || tokens[i] == 'Π'){
                values.push(Character.toString(tokens[i]));
            }else if (tokens[i] == '('
                    || tokens[i] == 'm' 
                    || tokens[i] == 'q' 
                    || tokens[i] == 'r' 
                    || tokens[i] == 's' 
                    || tokens[i] == 'c' 
                    || tokens[i] == 't' 
                    || tokens[i] == 'g' 
                    || tokens[i] == 'n' ) {
                operators.push(tokens[i]);
            } else if (tokens[i] == ')') {
                try{
                    // If the character is ')', pop and apply
                    // operators until '(' is encountered
                    while (operators.peek() != '(') {
                        values.push(applyOperator(
                                operators.pop(), values.pop(),
                                values.pop()));
                    }
                    operators.pop();
                    if(operators.peek() == 'm' 
                        || operators.peek() == 'q' 
                        || operators.peek() == 'r' 
                        || operators.peek() == 's' 
                        || operators.peek() == 'c' 
                        || operators.peek() == 't' 
                        || operators.peek() == 'g' 
                        || operators.peek() == 'n') {
                        values.push(applyFunction(operators.pop(),values.pop()));
                    }
                }    
                catch(Exception e){
                    rw.writeToFile(">>>> Error in processing () and functions\n" + e + "\n", "log", true);
                }
            } else if (tokens[i] == '+' 
                    || tokens[i] == '-'
                    || tokens[i] == '*'
                    || tokens[i] == '/' 
                    || tokens[i] == '^') {
                try{
                    // If the character is an operator, pop and
                    // apply operators with higher precedence
                    while (!operators.isEmpty() && hasPrecedence(tokens[i],
                                operators.peek())) {
                    values.push(applyOperator(
                            operators.pop(), values.pop(),
                            values.pop()));
                    }
                    // Push the current operator to the
                    // operators stack
                    operators.push(tokens[i]);
                }    
                catch(Exception e){
                    rw.writeToFile(">>>> Error in processing operators\n" + e + "\n", "log", true);
                }  
            }
        }
        try{
            // Process any remaining operators in the stack
            while (!operators.isEmpty()) {
                values.push(applyOperator(operators.pop(),
                        values.pop(),
                        values.pop()));
            }
        }    
        catch(Exception e){
            rw.writeToFile(">>>> Error in constructing processing any remaining operators \n" + e + "\n", "log", true);
        }
        return values.pop();
    }

    // Function to check if operator1 has higher precedence
    // than operator2
    private static boolean hasPrecedence(char operator1,
            char operator2) {
        try{
            if (operator2 == '(' || operator2 == ')') {
                return false;
            }
            if ( operator1 == '^') {
                return true;
            }
        }    
        catch(Exception e){
            rw.writeToFile(">>>> Error in hasPrecedence\n" + e + "\n", "log", true);
        }
        return (operator1 != '*' && operator1 != '/')
                || (operator2 != '+' && operator2 != '-');
    }

    private static String applyOperator(char operator,
            String B, String A) {
        try{
            double a = 0;
            double b = 0;
            switch (B){
                case "e":{
                    a = el;
                    break;
                }
                case "Π":{
                    b = pi;
                    break;
                }
                default:{
                    b = Double.parseDouble(B);
                }
            }
            switch (A){
                case "e":{
                    a = el;
                    break;
                }
                case "Π":{
                    a = pi;
                    break;
                }
                default:{
                    a = Double.parseDouble(A);
                }
            }
            switch (operator) {
                case '+':
                    return Double.toString(a + b);
                case '-':
                    return Double.toString(a - b);
                case '*':
                    return Double.toString(a * b);
                case '/':
                    if (b == 0) {
                        throw new ArithmeticException(
                                "Cannot divide by zero");
                    }
                    return Double.toString(a / b);
                case '^':
                    return Double.toString(Math.pow(a, b));
            }
        }
        catch(Exception e){
            rw.writeToFile(">>>> Error in applyOperator\n" + e + "\n", "log", true);
        }    
        return "0";
    }
    private static String applyProcent(char operator,
            String b, String a) {
        try{
            double A = Double.parseDouble(a);
            double B = Double.parseDouble(b);
            switch (operator) {
                case '+':
                    return Double.toString(A + (A * B / 100.0));
                case '-':
                    return Double.toString(A - (A * B / 100.0));
                case '*':
                    return Double.toString(A * (B / 100.0));
                case '/':
                    if (B == 0) {
                        return "0";
                    }
                   return Double.toString(A / (B / 100.0));
            }
        }
        catch(Exception e){
            rw.writeToFile(">>>> Error in applyProcent\n" + e + "\n", "log", true);
        }
        return "0";
    }
    private static String applyFunction(char function,
            String a) {
        try{
            double A = Double.parseDouble(a);
            switch (function) {
                case 'm':
                     return isRad?Double.toString(Math.asin(A)):Double.toString(Math.toDegrees(Math.asin(A)));
                case 'q': 
                    return isRad?Double.toString(Math.acos(A)):Double.toString(Math.toDegrees(Math.acos(A)));
                case 'r':
                    return isRad?Double.toString(Math.atan(A)):Double.toString(Math.toDegrees(Math.atan(A)));
                case 's':
                    if(A % 180 == 0)
                    {
                        return "0.0";
                    }
                    else
                    {
                        return isRad?Double.toString(Math.sin(A)):Double.toString(Math.sin(Math.toRadians(A)));
                    }
                case 'c':
                    if(A % 90 == 0 && A % 180 != 0)
                    {
                        return "0.0";
                    }
                    else
                    {
                        return isRad?Double.toString(Math.cos(A)):Double.toString(Math.cos(Math.toRadians(A)));
                    }
                case 't':
                    if(A % 180 == 0)
                    {
                        return "0.0";
                    }
                    else if (A % 45 == 0)
                    {
                        return "1.0";
                    }
                    else
                    {
                        return isRad?Double.toString(Math.tan(A)):Double.toString(Math.tan(Math.toRadians(A)));
                    }
                case 'g':
                    return Double.toString(Math.log10(A));
                case 'n':
                    return Double.toString(Math.log1p(A)); 
            }
        }
        catch(Exception e){
            rw.writeToFile(">>>> Error in applyFunction\n" + e + "\n", "log", true);
        }
        return "0";
    }
}
