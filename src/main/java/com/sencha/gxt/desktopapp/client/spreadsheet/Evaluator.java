/**
 * Sencha GXT 1.0.0-SNAPSHOT - Sencha for GWT
 * Copyright (c) 2006-2018, Sencha Inc.
 *
 * licensing@sencha.com
 * http://www.sencha.com/products/gxt/license/
 *
 * ================================================================================
 * Commercial License
 * ================================================================================
 * This version of Sencha GXT is licensed commercially and is the appropriate
 * option for the vast majority of use cases.
 *
 * Please see the Sencha GXT Licensing page at:
 * http://www.sencha.com/products/gxt/license/
 *
 * For clarification or additional options, please contact:
 * licensing@sencha.com
 * ================================================================================
 *
 *
 *
 *
 *
 *
 *
 *
 * ================================================================================
 * Disclaimer
 * ================================================================================
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 * ================================================================================
 */
package com.sencha.gxt.desktopapp.client.spreadsheet;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

/**
 * <pre>
 * expression =
 *     simple-expression
 *     | "-" expression
 *     | "+" expression
 *     | "=" expression .
 *     
 * simple-expression = term {("+"|"-") term} .
 * 
 * term = factor {("*"|"/") factor} .
 * 
 * factor =
 *     name
 *     | number
 *     | "(" expression ")" .
 *     
 * name =
 *     cell-reference
 *     | function-name "(" expression {"," expression} ")" .
 *     
 * </pre>
 * 
 * The leading '=' form of the expression is provided to simplify use of
 * EXPRESSION_MARKER in spreadsheet formulas.
 * 
 * As is the convention, the leading '+' form of the expression is provided for
 * symmetry with the leading '-' form.
 * 
 * A cell-reference takes the conventional column-name-row-number form, where
 * either can be specified as $ to indicate the same column or row, for example:
 * 
 * <pre>
 * =((B1-A1)/A1)*100 to calculate percent increase from A1 to B1
 * =SUM(B2:B6) to sum rows 2 through 6 in column B
 * =SUM($2:$6) to sum rows 2 through 6 in the same column
 * =SUM(B$:F$) to sum columns B through F in the same row
 * </pre>
 */
public final class Evaluator {

  public enum Location {
    COLUMN, ROW
  }

  public enum Operation {
    DELETE, INSERT, RESTORE, SAVE
  }

  public class RecursiveExpressionException extends IllegalArgumentException {

    public RecursiveExpressionException(String message) {
      super(message);
    }

  }

  public class Scanner {
    private int base;
    private int current;
    private String expression;

    public Scanner(String expression) {
      this.expression = expression;
    }

    public String getNext() {
      base = current;
      int length = expression.length();
      State state = State.INITIAL;
      StringBuilder s = new StringBuilder();
      char quote = 0;

      while (current < length && state != State.FINAL) {
        char c = expression.charAt(current);
        switch (state) {
          case INITIAL:
            if (isOperator(c)) {
              s.append(c);
              state = State.FINAL;
            } else if (isAlpha(c)) {
              s.append(c);
              state = State.IDENTIFIER;
            } else if (c == '.' || isDigit(c)) {
              s.append(c);
              state = State.NUMBER;
            } else if (c == '\'') {
              quote = c;
              state = State.QUOTE;
            } else if (!isWhitespace(c)) {
              throw new IllegalArgumentException("Unexpected character " + c);
            }
            break;
          case IDENTIFIER:
            if (isWhitespace(c)) {
              state = State.FINAL;
            } else if (isOperator(c)) {
              current--;
              state = State.FINAL;
            } else {
              s.append(c);
            }
            break;
          case NUMBER:
            if (c == '.' || isDigit(c)) {
              s.append(c);
            } else {
              current--;
              state = State.FINAL;
            }
            break;
          case QUOTE:
            if (c == quote) {
              state = State.FINAL;
            } else {
              s.append(c);
            }
            break;
          case FINAL:
            // do nothing
        }
        current++;
      }
      return s.toString();
    }

    public void retract() {
      current = base;
    }

    private boolean isAlpha(char c) {
      return Character.isLetter(c) || c == '$' || c == '@';
    }

    private boolean isDigit(char c) {
      return Character.isDigit(c);
    }

    private boolean isOperator(char c) {
      return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')' || c == ',' || c == ':' || c == '=';
    }

    private boolean isWhitespace(char c) {
      // Note: GWT does not support Character.isWhitespace
      return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }
  }

  public enum State {
    FINAL, IDENTIFIER, INITIAL, NUMBER, QUOTE
  }

  private class AvgVisitor implements Visitor {

    int count;
    double sum;

    public double getValue() {
      return count == 0 ? 0 : sum / count;
    }

    @Override
    public void initialize() {
      count = 0;
      sum = 0;
    }

    @Override
    public void visit(int rowIndex, int columnIndex) {
      count++;
      sum += getDouble(rowIndex, columnIndex);
    }

  }

  private class Cell {

    private String rowName;
    private int rowIndex;
    private String columnName;
    private int columnIndex;

    private Cell(String rowName, int rowIndex, String columnName, int columnIndex) {
      this.rowName = rowName;
      this.rowIndex = rowIndex;
      this.columnName = columnName;
      this.columnIndex = columnIndex;
    }

    private int getColumnIndex() {
      return columnIndex;
    }

    private String getColumnName() {
      return columnName;
    }

    private int getRowIndex() {
      return rowIndex;
    }

    private String getRowName() {
      return rowName;
    }

  }

  private class MaxVisitor implements Visitor {

    double max;

    public double getValue() {
      return max;
    }

    @Override
    public void initialize() {
      max = Double.MIN_NORMAL;
    }

    @Override
    public void visit(int rowIndex, int columnIndex) {
      max = Math.max(max, getDouble(rowIndex, columnIndex));
    }
  }

  private class MinVisitor implements Visitor {

    double min;

    public double getValue() {
      return min;
    }

    @Override
    public void initialize() {
      min = Double.MAX_VALUE;
    }

    @Override
    public void visit(int rowIndex, int columnIndex) {
      min = Math.min(min, getDouble(rowIndex, columnIndex));
    }
  }

  private class SumVisitor implements Visitor {

    double sum;

    public double getValue() {
      return sum;
    }

    @Override
    public void initialize() {
      sum = 0;
    }

    @Override
    public void visit(int rowIndex, int columnIndex) {
      sum += getDouble(rowIndex, columnIndex);
    }
  }

  private interface Visitor {

    public double getValue();

    public void initialize();

    public void visit(int rowIndex, int columnIndex);

  }

  public static final String EXPRESSION_MARKER = "=";
  private static final int MAX_LEVELS = 10;
  private TableValueProvider table;
  private Scanner scanner;
  private int level;
  private int rowIndex = -1;
  private int columnIndex = -1;

  public Evaluator(String expression, TableValueProvider table) {
    this(expression, table, 0);
  }

  public Evaluator(String expression, TableValueProvider table, int level) {
    this.scanner = new Scanner(expression);
    this.table = table;
    this.level = level;
  }

  public String adjustCellReferences(Operation operation, Location location, String expression, int index) {
    int delta = 0;
    switch (operation) {
      case DELETE:
        delta = -1;
        break;
      case INSERT:
        delta = +1;
        break;
      default:
        // do nothing
    }
    StringBuilder newExpression = new StringBuilder();
    Scanner scanner = new Scanner(expression);
    String token;
    while (!(token = scanner.getNext()).isEmpty()) {
      Cell cell = parseCellReference(token);
      if (cell == null) {
        newExpression.append(token);
      } else {
        switch (location) {
          case COLUMN:
            if (cell.getColumnName().equals("$")) {
              newExpression.append(token);
            } else if (operation == Operation.SAVE && cell.getColumnIndex() == index) {
              newExpression.append(SpreadsheetUtilities.getCellName(cell.getRowName(), "@"));
            } else if (operation == Operation.RESTORE && cell.getColumnName().equals("@")) {
              newExpression.append(SpreadsheetUtilities.getCellName(cell.getRowName(), index));
            } else if (cell.getColumnIndex() < index) {
              newExpression.append(token);
            } else {
              newExpression.append(SpreadsheetUtilities.getCellName(cell.getRowName(), cell.getColumnIndex() + delta));
            }
            break;
          case ROW:
            if (cell.getRowName().equals("$")) {
              newExpression.append(token);
            } else if (operation == Operation.SAVE && cell.getRowIndex() == index) {
              newExpression.append(SpreadsheetUtilities.getCellName("@", cell.getColumnName()));
            } else if (operation == Operation.RESTORE && cell.getRowName().equals("@")) {
              newExpression.append(SpreadsheetUtilities.getCellName(index, cell.getColumnName()));
            } else if (cell.getRowIndex() < index) {
              newExpression.append(token);
            } else {
              newExpression.append(SpreadsheetUtilities.getCellName(cell.getRowIndex() + delta, cell.getColumnName()));
            }
            break;
          default:
            throw new UnsupportedOperationException();
        }
      }
    }
    return newExpression.toString();
  }

  public double evaluateExpression() {
    double left;
    String operator = getOperator("-", "+", "=");
    if (operator == null) {
      left = evaluateSimpleExpression();
    } else if (operator.equals("+") || operator.equals("=")) {
      left = evaluateExpression();
    } else {
      left = -evaluateExpression();
    }
    return left;
  }

  public double getDouble(String stringValue, int rowIndex, int columnIndex) {
    double doubleValue;
    if (stringValue.startsWith("=")) {
      if (level > MAX_LEVELS) {
        throw new RecursiveExpressionException(stringValue);
      }
      Evaluator evaluator = new Evaluator(stringValue, table, level + 1);
      evaluator.setRowIndex(rowIndex);
      evaluator.setColumnIndex(columnIndex);
      doubleValue = evaluator.evaluateExpression();
    } else {
      doubleValue = toDouble(stringValue);
    }
    return doubleValue;
  }

  public void setColumnIndex(int columnIndex) {
    this.columnIndex = columnIndex;
  }

  public void setRowIndex(int rowIndex) {
    this.rowIndex = rowIndex;
  }

  private double evaluateCellRangeFunctionReference(String token) {
    Visitor visitor = null;
    if (token.equalsIgnoreCase("avg")) {
      visitor = new AvgVisitor();
    } else if (token.equalsIgnoreCase("max")) {
      visitor = new MaxVisitor();
    } else if (token.equalsIgnoreCase("min")) {
      visitor = new MinVisitor();
    } else if (token.equalsIgnoreCase("sum")) {
      visitor = new SumVisitor();
    } else {
      throw new IllegalArgumentException("Expected AVG, MAX, MIN or SUM. Encountered " + token);
    }
    visitCellRange(visitor);
    return visitor.getValue();
  }

  private double evaluateCellReference(String token) {
    Cell cell = parseCellReference(token);
    if (cell == null) {
      throw new IllegalArgumentException("Invalid cell reference " + token + ". Please use ColRow notation (e.g. B1).");
    }
    String value = table.getValue(cell.getRowIndex(), cell.getColumnIndex());
    return getDouble(value, cell.getRowIndex(), cell.getColumnIndex());
  }

  private double evaluateFactor() {
    double value;
    String token = scanner.getNext();
    if (isName(token)) {
      String lookahead = scanner.getNext();
      if (lookahead.equals("(")) {
        value = evaluateCellRangeFunctionReference(token);
        expect(")");
      } else {
        scanner.retract();
        value = evaluateCellReference(token);
      }
    } else if (token.equals("(")) {
      value = evaluateExpression();
      expect(")");
    } else {
      value = toDouble(token);
    }
    return value;
  }

  private double evaluateSimpleExpression() {
    double left = evaluateTerm();
    String operator;
    while ((operator = getOperator("+", "-")) != null) {
      double right = evaluateTerm();
      if (operator.equals("+")) {
        left += right;
      } else if (operator.equals("-")) {
        left -= right;
      }
    }
    return left;
  }

  private double evaluateTerm() {
    double left = evaluateFactor();
    String operator;
    while ((operator = getOperator("*", "/")) != null) {
      double right = evaluateFactor();
      if (operator.equals("*")) {
        left *= right;
      } else if (operator.equals("/")) {
        left /= right;
      }
    }
    return left;
  }

  private void expect(String expectedToken) {
    String token = scanner.getNext();
    if (!token.equals(expectedToken)) {
      throw new IllegalArgumentException("Expected " + expectedToken + ". Encountered " + token);
    }
  }

  private double getDouble(int rowIndex, int columnIndex) {
    String value = table.getValue(rowIndex, columnIndex);
    return getDouble(value, rowIndex, columnIndex);
  }

  private String getOperator(String... operators) {
    String token = scanner.getNext();
    for (String operator : operators) {
      if (token.equals(operator)) {
        return operator;
      }
    }
    scanner.retract();
    return null;
  }

  private boolean isName(String token) {
    return Character.isLetter(token.charAt(0));
  }

  private Cell parseCellReference(String cellName) {
    Cell cell = null;
    String pattern = "^([a-zA-Z\\$\\@])+([0-9\\$\\@])+$";
    RegExp regularExpression = RegExp.compile(pattern);
    MatchResult matchResult = regularExpression.exec(cellName);
    if (matchResult != null) {
      String columnName = matchResult.getGroup(1);
      int columnIndex;
      if (columnName.equals("$") || columnName.equals("@")) {
        columnIndex = this.columnIndex;
      } else {
        columnIndex = SpreadsheetUtilities.getColumnIndex(columnName);
      }
      String rowName = matchResult.getGroup(2);
      int rowIndex;
      if (rowName.equals("$") || rowName.equals("@")) {
        rowIndex = this.rowIndex;
      } else {
        rowIndex = Integer.parseInt(rowName) - 1;
      }
      cell = new Cell(rowName, rowIndex, columnName, columnIndex);
    }
    return cell;
  }

  private double toDouble(String token) {
    double value;
    try {
      value = Double.parseDouble(token);
    } catch (NumberFormatException e) {
      value = 0;
    }
    return value;
  }

  private void visitCellRange(Visitor visitor) {
    String from = scanner.getNext();
    Cell fromCell = parseCellReference(from);
    expect(":");
    String to = scanner.getNext();
    Cell toCell = parseCellReference(to);
    visitor.initialize();
    for (int rowIndex = fromCell.getRowIndex(); rowIndex <= toCell.getRowIndex(); rowIndex++) {
      for (int columnIndex = fromCell.getColumnIndex(); columnIndex <= toCell.getColumnIndex(); columnIndex++) {
        visitor.visit(rowIndex, columnIndex);
      }
    }
  }
}
