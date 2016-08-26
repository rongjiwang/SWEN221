package swen221.assignment3.shapes;

import java.util.*;

/**
 * Responsible for interpreting a shape program. The program is represented as a
 * string, through which the interpreter moves. For example, consider this shape
 * program:
 * 
 * <pre>
 * x =[0,0,10,10]
 * fill x #000000
 * </pre>
 * 
 * This program will be represented in the input string as follows:
 * 
 * <pre>
 * --------------------------------------------------------------
 * | x |   | = | [ | 0 | , | 0 | , | 1 | 0 | , | 1 | 0 | ] | \n |
 * --------------------------------------------------------------
 *   0   1   2   3   4   5   6   7   8   9   10  11  12  13  14
 * 
 * (continued)
 * --------------------------------------------------------------
 * | f | i | l | l |   | x |   | # | 0 | 0 | 0 | 0 | 0 | 0 | \n |
 * --------------------------------------------------------------
 *   14  15  16  17  18  19  20  21  22  23  24  25  26  27  38
 * </pre>
 * 
 * The interpreter starts at index 0 and attempts to decide what kind of command
 * we have. If the first four characters are "fill" then it's a fill command. If
 * the first four characters are "draw" then it's a draw command. Otherwise, it
 * must be an assignment command.
 * 
 * @author Rongji Wang
 *
 */
public class Interpreter {
	/**
	 * The input program being interpreted by this class
	 */
	private String input;
	// private Map<String, Shape> map = new HashMap<>();

	/**
	 * The current position within the input program that this interpreter has
	 * reached.
	 */
	private int index; // current position within the input string.

	/**
	 * A mapping from variables to their shape value. When a variable is
	 * assigned a new shape value, then this map is updated accordingly.
	 */
	private HashMap<String, Shape> environment = new HashMap<String, Shape>();

	/**
	 * Construct an interpreter from a given input string representing a simple
	 * shape program.
	 *
	 * @param input
	 */
	public Interpreter(String input) {
		this.input = input;
		this.index = 0;
	}

	/**
	 * This method creates an empty canvas onto which it evaluates each command
	 * of the program in turn. The canvas is then returned.
	 *
	 * @return a canvas that shows the result of the input.
	 */
	public Canvas run() {
		Canvas canvas = new Canvas();
		while (index < input.length()) {
			evaluateNextCommand(canvas);
		}
		return canvas;
	}

	/**
	 * Evaluate the next command in the program. To do this, the interpreter
	 * must first decide what kind of command it is. This is done by looking at
	 * the first word of the input string at the current position.
	 *
	 * @param canvas
	 */
	private void evaluateNextCommand(Canvas canvas) {
		skipWhiteSpace();
		String cmd = readWord();
		if (cmd == null) {
			index++;
			return;
		}
		if (cmd.equalsIgnoreCase("fill")) {
			Shape shape = evaluateShapeExpression(); // draw shape
			Color color = readColor();
			fillShape(color, shape, canvas);

		} else if (cmd.equalsIgnoreCase("draw")) {
			Shape shape = evaluateShapeExpression(); // fill shape
			Color color = readColor();
			drawShape(color, shape, canvas);

		} else {

			if (input.charAt(index) == '=') {
				index++;
				Shape newShape = evaluateShapeExpression(); // cast data to
															// shape
				if (newShape != null) {

					environment.put(cmd, newShape); // store to HashMap
				} else
					throw new IllegalArgumentException();
			}
		}

	}

	/**
	 * Read a "word" from the input string. This is defined as a sequence of one
	 * or more consequtive letters. Digits and other characters (e.g. '_' or
	 * '+') are not permitted as part of a word.
	 *
	 * @return
	 */
	private String readWord() {

		int start = index;
		String string = null;
		// System.out.println(index + " find the out readword index num");
		while (index < input.length()
				&& (Character.isLetter(input.charAt(index)) || Character.isDigit(input.charAt(index)))) {
			string = input.substring(start, index + 1);
			// System.out.println(input.charAt(index) + "- readWord");
			index++;
		}
		// string = input.substring(start, index);
		// System.out.println(string + " ++");
		skipWhiteSpace();

		return string;

	}

	/**
	 * This should fill a given shape in a given colour onto the canvas.
	 *
	 * @param color
	 * @param shape
	 * @param canvas
	 */
	public void fillShape(Color color, Shape shape, Canvas canvas) {
		// TODO: For part 1 you'll need to complete this
		if (shape.boundingBox() instanceof Rectangle) {
			Rectangle rec = shape.boundingBox();
			// fill within the box
			for (int i = rec.getX(); i < (rec.getX() + rec.getWidth()); i++) {
				for (int j = rec.getY(); j < (rec.getY() + rec.getHeight()); j++) {
					if (shape.contains(i, j)) {
						canvas.draw(i, j, color);
					}
				}
			}
		}
	}

	/**
	 * This should draw a given shape in a given colour onto the canvas.
	 *
	 * @param color
	 * @param shape
	 * @param canvas
	 */
	public void drawShape(Color color, Shape shape, Canvas canvas) {
		// TODO: For part 1 you'll need to complete this
		if (shape.boundingBox() instanceof Rectangle) {
			Rectangle rec = shape.boundingBox();

			int endX = rec.getX() + rec.getWidth();
			int endY = rec.getY() + rec.getHeight();
			// System.out.println(rec.getX() + " " + rec.getY() + " " + endX + "

			for (int i = rec.getX(); i < endX; i++) {
				for (int j = rec.getY(); j < endY; j++) {
					// draw the edge of the box
					if (shape.contains(i, j) && !shape.contains(i + 1, j)) {
						canvas.draw(i, j, color);
					} else if (shape.contains(i, j) && !shape.contains(i - 1, j)) {
						canvas.draw(i, j, color);
					} else if (shape.contains(i, j) && !shape.contains(i, j + 1)) {
						canvas.draw(i, j, color);
					} else if (shape.contains(i, j) && !shape.contains(i, j - 1)) {
						canvas.draw(i, j, color);
					}

					// System.out.println(i + " " + j + "--5555555555");
				}
			}
		} else {
			this.error("Can not fill non-Rectangle shape");
		}

	}

	/**
	 * Evaluate a shape expression which is expected at the current position
	 * within the input string. This is done by first looking at the current
	 * character in the input string. If this is a '(', for example, then it
	 * signals the start of a bracketed expression.
	 *
	 * @return Shape
	 */
	public Shape evaluateShapeExpression() {
		skipWhiteSpace();
		// System.out.println(index + " ***");
		char lookahead = input.charAt(index);
		// System.out.println(lookahead + " ***");

		Shape value = null;

		if (lookahead == '(') {
			// in this case, we have a bracketed sub-expression
			value = evaluateBracketedExpression();

		} else { // cast to Shape when read '[' or string
			// in this case, we have a bracketed sub-expression
			value = evaluateRectangleExpression();
		}

		skipWhiteSpace();
		if (index < input.length() && String.valueOf(input.charAt(index)).matches("^[-+&]$")) {

			lookahead = Character.valueOf(input.charAt(index));
			if (lookahead == '+') { // update return shape when read'+'

				index++;
				value = new ShapeUnion(value, evaluateShapeExpression());

			} else if (lookahead == '&') { // update return shape when read'&'

				index++;
				value = new ShapeIntersection(value, evaluateShapeExpression());
			} else if (lookahead == '-') { // update return shape when read'-'

				index++;
				value = new ShapeDifference(value, evaluateShapeExpression());
			} else {
				throw new IllegalArgumentException();
			}
		}
		// System.out.println(index + " ///");
		// System.out.println(value + "***/");
		return value;
	}

	/**
	 * Evaluate a bracketed shape expression. That is a shape expression which
	 * is surrounded by braces.
	 *
	 * @return Shape
	 */
	private Shape evaluateBracketedExpression() {

		index++;
		Shape shape = evaluateShapeExpression();
		if (index < input.length() && input.charAt(index) == ')') {

			index++;
			return shape;
		} else {
			return null;
		}
	}

	/**
	 * Evaluate a rectangle expression. That is, four numbers separated by
	 * comma's and '[', ']'.
	 *
	 * @return
	 */
	private Shape evaluateRectangleExpression() {

		skipWhiteSpace();
		if (input.charAt(index) == '[') { // start reading inside '[' ']'
			int start = ++index;
			String string = "";
			int i = 0;
			for (i = start; i < input.length(); i++) {
				if (input.charAt(i) != ']') { // gether the data as string
					string += input.charAt(i);
				} else {
					break;
				}
			}
			index = i;
			String[] num = string.split(",");
			if (num.length == 4) {
				int x = Integer.parseInt(num[0].trim());
				int y = Integer.parseInt(num[1].trim()); // cast string data to
															// integer
				int width = Integer.parseInt(num[2].trim());
				int height = Integer.parseInt(num[3].trim());

				if (x >= 0 && y >= 0 && width >= 0 && height >= 0) {

					// System.out.println(x + " " + y + " " + width + " " +
					// height);
					index++;
					return new Rectangle(x, y, width, height);
				}

			} else {
				throw new IllegalArgumentException();
			}
		} else {
			String var = readWord(); // not start as '[' must be a string value

			if (environment.containsKey(var)) { // eg. x = y, search x shape in
												// map and return to y

				return environment.get(var);
			} else {
				throw new IllegalArgumentException();
			}
		}

		return null;
	}

	/**
	 * Read a color which is expected at the current input position. A color is
	 * a string of 7 characters, of which the first is a '#' and the remainder
	 * are digits or letters.
	 *
	 * @return
	 */
	public Color readColor() {
		// System.out.println(index + " " + input.charAt(index) + "+\n");

		skipWhiteSpace();

		if ((index + 7) > input.length()) {
			error("expecting color");
		}
		String str = input.substring(index, index + 7);
		// System.out.println(str);
		index += 7;
		// System.out.println(index+" "+ input.charAt(index)+"+\n");

		return new Color(str);
	}

	/**
	 * Skip over any "whitespace" at the current input position. That is, any
	 * sequence of zero or more space or newline characters.
	 */
	private void skipWhiteSpace() {
		while (index < input.length() && (input.charAt(index) == ' ' || input.charAt(index) == '\n')) {
			index = index + 1;
		}
	}

	/**
	 * Report an error
	 *
	 * @param error
	 */
	private void error(String error) {
		String msg = error + "\n" + input + "\n";
		for (int i = 0; i < index; ++i) {
			msg += " ";
		}
		msg += "^";
		throw new IllegalArgumentException(msg);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Interpreter inter = new Interpreter("x = [2,2,4,4]\nfill x #0000ff\n");
		Canvas c = inter.run();
		c.show();
	}
}
