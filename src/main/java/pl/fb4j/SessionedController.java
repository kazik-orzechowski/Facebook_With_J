package pl.fb4j;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * SessionedController is used to create http session.
 * 
 * @author kaz
 *
 */
public class SessionedController {

	/**
	 * Creates http session.
	 * 
	 * @return http session
	 */
	public static HttpSession session() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return attr.getRequest().getSession(true);
		// true == allow to create
	}
}
