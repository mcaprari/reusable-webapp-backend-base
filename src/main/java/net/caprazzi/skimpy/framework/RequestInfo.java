package net.caprazzi.skimpy.framework;

import javax.servlet.http.HttpServletRequest;

/**
 * Utility class to quickly check and read request information such as method and path
 * @author matteo.caprari@gmail.com
 */
public final class RequestInfo {
	
	private final String[] parts;
	private final String method;
	private final boolean endsWithSlash;

	public RequestInfo(String method, String uri) {
		this.method = method;
		this.parts = uri.substring(1).split("/");
		this.endsWithSlash = uri.endsWith("/");
	}
	
	public static RequestInfo fromRequest(HttpServletRequest req) { 
		return new RequestInfo(req.getMethod(), req.getRequestURI());
	}
	
	/**
	 * Check id request method is GET
	 * @return
	 */
	private boolean isGet() {
		return method.equals("GET");
	}

	/**
	 * Returns the path part at position pos. Throws a RuntimeException if the part does not exist.
	 * @param pos
	 * @return
	 */
	public String get(int pos) {
		if (parts.length >= pos-1)
			return parts[pos];
		throw new RuntimeException("Part " + pos + " does not exist in current request");
	}

	/**
	 * Verify if request method is PUT
	 * @return
	 */
	public boolean isPut() {
		return method.equals("PUT");
	}

	/**
	 * Verify if request method is POST
	 * @return
	 */
	public boolean isPost() {
		return method.equals("POST");
	}

	/**
	 * Check if request method is GET and matches expression. @see isPath for expressions
	 * @param expression
	 * @return
	 */
	public boolean isGet(String expression) {
		return isGet() && isPath(expression);
	}
	
	/**
	 * Check if request method is PUT and matches expression. @see isPath for expressions
	 * @param expression
	 * @return
	 */
	public boolean isPut(String expression) {
		return isPut() && isPath(expression);
	}
	
	/**
	 * Check if request method is POST and matches expression. @see isPath for expressions
	 * @param expression
	 * @return
	 */
	public boolean isPost(String expression) {
		return isPost() && isPath(expression);
	}

	/**
	 * Checks is current request matches an expression. This is very limited in functionality, and the only available wildcard is _
	 * Examples:<pre>
	 * URI: /users/123 
	 * /users/_: true
	 * /users/_/: false
	 * /_/123: true
	 * /_/123/: false
	 * /_/_: true
	 * /_/_/: false
	 * </pre>
	 * @param expression
	 * @return
	 */
	public boolean isPath(String expression) {		
		String[] checkParts = expression.substring(1).split("/");
		if (checkParts.length != parts.length)
			return false;
		
		if(expression.endsWith("/") != endsWithSlash)
			return false;
		
		for(int pos=0; pos<checkParts.length; pos++) {
			if (!checkParts[pos].equals("_") && !checkParts[pos].equals(parts[pos]))
				return false;
		}
		
		return true;
	}

}