<!--
This file is part of Termitaria, a project management tool 
 Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
  
 Termitaria is free software; you can redistribute it and/or 
 modify it under the terms of the GNU Affero General Public License 
 as published by the Free Software Foundation; either version 3 of 
 the License, or (at your option) any later version.
 
 This program is distributed in the hope that it will be useful, 
 but WITHOUT ANY WARRANTY; without even the implied warranty of 
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 GNU Affero General Public License for more details.
 
 You should have received a copy of the GNU Affero General Public License 
 along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
-->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<%@ include file="../Taglibs.jsp"%>
<%@ page import="java.util.List"%>


<%@page import="java.util.Locale"%>

<head>
<title><spring:message code="title" /></title>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<!-- CSS Section -->
<link rel="stylesheet" type="text/css"
	href="themes/standard/css/style.css" />
<script type="text/javascript" src="js/jquery/jquery-1.9.0.min.js"></script>
</head>

<body>

	<div id="loginContainer">
		<div id="login">
			<img src="images/logos/logoSuite.png" style="display: block" />
		</div>
		<div id="loginContent">

			<div id="MODULES_DIV">
				<center>
					<font class="welcomeMessage"><spring:message code="welcome" />,
						<span
						title="<security:authentication property="principal.fullName"/>">
							<security:authentication property="principal.firstNameTruncate" />
							<security:authentication property="principal.lastNameTruncate" />
					</span> !</font><br> <br /> <span class="chooseModuleMessage"><spring:message
								code="please.choose.module" /></span> <br /> <br /> <c:choose>
							<c:when test="${empty modules}">
								<spring:message code="zero.modules" />
								<br />
							</c:when>
							<c:otherwise>
								<table id="loginProducts" align="center">
									<tr>
										<c:set var="i" value="0" />
										<c:forEach var="module" items="${modules}">
											<c:set var="i" value="${i+1}" />
											<c:choose>
												<c:when test="${module.moduleId != OM_MODULE_ID}">
													<!-- Not display DM or IR for adminIT -->
													<c:if test="${IS_ADMIN_IT eq false}">
														<td>
															<div id="leaf_module">
																<form name="gotoModule${i}Form" action="${module.url}"
																	method="get">
																	<input type="hidden" name="securityToken"
																		id="token${i }" /> <input type="hidden"
																		name="moduleId" value="${module.moduleId}" /> <input
																		type="hidden" name="siteLanguage"
																		value="${siteLanguage}" /> <a href="#"
																		style="text-decoration: none; _position: relative; _left: -10px;"
																		onClick="document.gotoModule${i}Form.submit()"
																		alt="${module.alt}"> <span> ${module.name}
																	</span> <img src="images/logos/product_${module.moduleId}.png" />
																	</a>
																</form>
															</div>
														</td>
													</c:if>
												</c:when>
												<c:otherwise>
													<td>
														<div id="leaf_module">
															<form name="gotoModule${i}Form" action="${module.url}"
																method="get">
																<input type="hidden" name="securityToken"
																	id="token${i }" /> <input type="hidden" name="moduleId"
																	value="${module.moduleId}" /> <input type="hidden"
																	name="siteLanguage" value="${siteLanguage}" /> <a
																	href="#"
																	style="text-decoration: none; _position: relative; _left: -10px;"
																	onClick="document.gotoModule${i}Form.submit()"
																	alt="${module.alt}"> <span> ${module.name} </span>
																	<img src="images/logos/product_${module.moduleId}.png" />
																</a>
															</form>
														</div>
													</td>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</tr>
								</table>
							</c:otherwise>
						</c:choose> <br /> <a href="j_spring_security_logout"><spring:message
								code="signout" /></a>
				</center>
			</div>
			<!-- end MODULES_DIV -->

		</div>
		<!-- end loginContent -->

	</div>


	<script>
		var url = "/Termitaria/OM/getSecurityToken.htm";

		$.post(url, {
			username : "${username}"
		}, function(data) {
			$('[name="securityToken"]').val(data);
		});
	</script>
</body>
</html>
