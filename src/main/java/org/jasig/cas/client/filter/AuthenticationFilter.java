package org.jasig.cas.client.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.DefaultGatewayResolverImpl;
import org.jasig.cas.client.authentication.GatewayResolver;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;  
/** 
 * 为了方便控制filter，自定义了统一认证过滤器AuthenticationFilter 
 * @author Administrator 
 * 
 */  
public class AuthenticationFilter extends AbstractCasFilter{  
     /** 
     * The URL to the CAS Server login. 
     */  
    private String casServerLoginUrl;  
  
    /** 
     * Whether to send the renew request or not. 
     */  
    private boolean renew = false;  
  
    /** 
     * Whether to send the gateway request or not. 
     */  
    private boolean gateway = false;  
    /** 
     * 添加属性，这里用来存放不过滤地址正则表达式，可以根据自己需求定制---1 
     */  
    private String excludePaths;  
      
    private GatewayResolver gatewayStorage = new DefaultGatewayResolverImpl();  
  
    protected void initInternal(final FilterConfig filterConfig) throws ServletException {  
        if (!isIgnoreInitConfiguration()) {  
            super.initInternal(filterConfig);  
            setCasServerLoginUrl(getPropertyFromInitParams(filterConfig, "casServerLoginUrl", null));  
            log.trace("Loaded CasServerLoginUrl parameter: " + this.casServerLoginUrl);  
            setRenew(parseBoolean(getPropertyFromInitParams(filterConfig, "renew", "false")));  
            log.trace("Loaded renew parameter: " + this.renew);  
            setGateway(parseBoolean(getPropertyFromInitParams(filterConfig, "gateway", "false")));  
            log.trace("Loaded gateway parameter: " + this.gateway);  
  
            final String gatewayStorageClass = getPropertyFromInitParams(filterConfig, "gatewayStorageClass", null);  
  
            if (gatewayStorageClass != null) {  
                try {  
                    this.gatewayStorage = (GatewayResolver) Class.forName(gatewayStorageClass).newInstance();  
                } catch (final Exception e) {  
                    log.error(e,e);  
                    throw new ServletException(e);  
                }  
            }  
            //自定义添加代码，用来读取web配置文件中excludes属性值 ---2  
            excludePaths = getPropertyFromInitParams(filterConfig, "excludePaths", null);//filterConfig.getInitParameter("excludePaths");  
            excludePaths = excludePaths.trim();  
        }  
    }  
  
    public void init() {  
        super.init();  
        CommonUtils.assertNotNull(this.casServerLoginUrl, "casServerLoginUrl cannot be null.");  
    }  
// url判断逻辑，这里大家可以根据自己需要来制订规则  
    private boolean isExclude(String uri){  
        boolean isInWhiteList = false;  
        if(excludePaths!=null&& uri!=null){  
        	String[] arrayExcludePaths = excludePaths.split("\\|\\|");
        	
        	for(String path:arrayExcludePaths){
        		isInWhiteList = uri.matches(path);
        		if(isInWhiteList){
        			return isInWhiteList;
        		}
        	}
              
        }  
        return isInWhiteList;  
    }  
     
      
    public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {  
        final HttpServletRequest request = (HttpServletRequest) servletRequest;  
        final HttpServletResponse response = (HttpServletResponse) servletResponse;  
        final HttpSession session = request.getSession(false);  
        final Assertion assertion = session != null ? (Assertion) session.getAttribute(CONST_CAS_ASSERTION) : null;  

        
        // 该判断是自定义的对符合条件的url进行通过处理 ---3  
        if(isExclude(request.getRequestURI())){  
            filterChain.doFilter(request, response);  
            return;  
        }  
          
        if (assertion != null) {  
        	
            filterChain.doFilter(request, response);  
            return;  
        }  
  
        final String serviceUrl = constructServiceUrl(request, response);  
        final String ticket = CommonUtils.safeGetParameter(request,getArtifactParameterName());  
        final boolean wasGatewayed = this.gatewayStorage.hasGatewayedAlready(request, serviceUrl);  
  
        if (CommonUtils.isNotBlank(ticket) || wasGatewayed) {  
            filterChain.doFilter(request, response);  
            return;  
        }  
  
        final String modifiedServiceUrl;  
  
        log.debug("no ticket and no assertion found");  
        if (this.gateway) {  
            log.debug("setting gateway attribute in session");  
            modifiedServiceUrl = this.gatewayStorage.storeGatewayInformation(request, serviceUrl);  
        } else {  
            modifiedServiceUrl = serviceUrl;  
        }  
  
        if (log.isDebugEnabled()) {  
            log.debug("Constructed service url: " + modifiedServiceUrl);  
        }  
  
        final String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.casServerLoginUrl, getServiceParameterName(), modifiedServiceUrl, this.renew, this.gateway);  
  
        if (log.isDebugEnabled()) {  
            log.debug("redirecting to \"" + urlToRedirectTo + "\"");  
        }  
  
        response.sendRedirect(urlToRedirectTo);  
    }  
  
    public final void setRenew(final boolean renew) {  
        this.renew = renew;  
    }  
  
    public final void setGateway(final boolean gateway) {  
        this.gateway = gateway;  
    }  
  
    public final void setCasServerLoginUrl(final String casServerLoginUrl) {  
        this.casServerLoginUrl = casServerLoginUrl;  
    }  
      
    public final void setGatewayStorage(final GatewayResolver gatewayStorage) {  
        this.gatewayStorage = gatewayStorage;  
    }  
      
}  