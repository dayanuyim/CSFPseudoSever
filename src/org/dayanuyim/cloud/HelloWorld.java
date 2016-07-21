package org.dayanuyim.cloud;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloWorld
 */
@WebServlet({ "/HelloWorld" })
public class HelloWorld extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public HelloWorld() {
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		String url = "/index.html";
		
		// get current action
		String action = request.getParameter("action");
		if (action == null) {
			action = "join"; // default action
		}

		// perform action and set URL to appropriate page
		if (action.equals("join")) {
			url = "/index.jsp"; // the "join" page
		}
		else if (action.equals("add")) {
			// get parameters from the request
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String email = request.getParameter("email");

			// store data in User object and save User object in database
			User user = new User(lastName, firstName, email);
			//UserDB.insert(user);
			
			String message;
			if(!email.contains("@") || !email.contains(".")){
				message = "bad email format";
				url = "/index.jsp"; // the "thanks" page

			}
			else{
				System.out.println(user);
				message = "";
				url = "/thanks.jsp"; // the "thanks" page
			}

			// set User object in request object and set URL
			request.setAttribute("user", user);
			request.setAttribute("message", message);
		}
		// forward request and response objects to specified URL
		getServletContext()
		.getRequestDispatcher(url)
		.forward(request, response);
	}

}
