import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Point_24 {
	public static int[] number = new int[4];//游戏使用到的四个数字
	public static Queue<String> solution = new LinkedList<String>();//用一个队列保存所有答案
	
	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		String command = null;
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("nashorn");
        String result = null;
		while(true) {
			generateNumber();
			if (!existSolution()) {//有可能会生成没有答案的四个数字，这时直接跳过
				continue;
			}
			System.out.println(number[0] + " " + number[1] + " " + number[2] + " " + number[3]);
			System.out.println("请输入你的答案，或者输入'n'进行下一局，或者输入'q'回车后结束游戏");
			command = in.nextLine();
			while (command == null || (!command.equals("q") && !command.equals("n") && !legalExpression(command))) {
				System.out.println("非法输入，请重新输入");
				command = in.nextLine();
			}
			if (command.equals("q")) {
				break;
			}
			
			if (command.equals("n")) {
				continue;
			}
			
			try {
				result = String.valueOf(scriptEngine.eval(command));
				if (result.equals("24")) {
					System.out.println("答案正确！");
				} 
				else {
					System.out.println("答案错误");
					//showAnswers();
				}
				showAnswers();
			} catch (Exception e) {
				//e.printStackTrace();
				System.out.println("表达式不合法");
			}
		}
		System.out.println("游戏结束");
		in.close();
	}
	
	/*随机生成四个1到13的数字*/
	public static void generateNumber() {
		Random random = new Random();
		number[0] = random.nextInt(13) + 1;
		number[1] = random.nextInt(13) + 1;
		number[2] = random.nextInt(13) + 1;
		number[3] = random.nextInt(13) + 1;
		Arrays.sort(number);
	}
	
	/*判断玩家输入的表达式是否合法*/
	public static boolean legalExpression(String expression) {
		char ch;
		int i = 0;
		int j = 0;
		int[] num = new int[4];
		while (i < expression.length()) {//这个循环获取玩家输入的表达式中所有的数字
			if (expression.charAt(i) < '0' || expression.charAt(i) > '9') {
				i++;
				continue;
			}
			while (i < expression.length() && expression.charAt(i) >= '0' && expression.charAt(i) <= '9') {
				ch = expression.charAt(i);
				num[j] *= 10;
				num[j] += ch - '0';
				i++;
			}
			j++;
		}
		if (j != 4) return false;//如果数字数量不为4直接判断为输入不合法
		Arrays.sort(num);
		for (i=0; i<4; i++) {//如果四个数字不和给定的四个数字相等也判断为不合法
			if (num[i] != number[i]) {
				return false;
			}
		}
		try {//判断输入是否是一个正确的表达式
			ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("nashorn");
			String.valueOf(scriptEngine.eval(expression));
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
	
	/*对于任意四个数字，判断这四个数字能否计算到24*/
	/*这里使用穷举的办法，效率比较低*/
	public static boolean existSolution() {
		while (!solution.isEmpty()) {
			solution.poll();
		}
		for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				for (int k=0; k<4; k++) {
					for (int l=0; l<4; l++) {
						if (i==j || i==k || i==l || j==k || j==l || k==l) continue;
						findAnswer(number[i], number[j], number[k], number[l]);
					}
				}
			}
		}
		if (solution.isEmpty()) {
			return false;
		}
		return true;
	}
	
	/*对于固定顺序的四个数字穷举三个运算符的所有可能性，效率比较低*/
	public static void findAnswer(int a, int b, int c, int d) {
		char[] le = new char[4];
		le[0] = '+';
		le[1] = '-';
		le[2] = '*';
		le[3] = '/';
		String str = null;
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("nashorn");
		for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				for (int k=0; k<4; k++) {
					str = "" + a + le[i] + b + le[j] + c + le[k] + d; 
					try {
						if (String.valueOf(scriptEngine.eval(str)).equals("24")) {
							solution.offer(str);
						}
					} catch (ScriptException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/*输出答案队列的所有答案，由于算法不高明，会有很多重复答案*/
	public static void showAnswers() {
		System.out.println("参考答案：");
		while (!solution.isEmpty()) {
			String answer = solution.poll();
			System.out.println(answer);
		}
		System.out.println();
	}
}

