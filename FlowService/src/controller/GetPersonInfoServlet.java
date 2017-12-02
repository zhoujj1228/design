package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetPersonInfoServlet extends HttpServlet{

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	protected void doGet(HttpServletRequest req, HttpServletResponse rsp) throws ServletException, IOException{
		PrintWriter out = rsp.getWriter();
		String musicID = req.getParameter("musciID");
		out.print("<audio autoplay = \"autoplay\">\n<source src = \"music/b.mp3\" type = \"audio/mpeg\"/>\n</audio>");
		out.flush();
		out.close();
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse rsp) throws ServletException, IOException{
		doGet(req, rsp);
	}

}
