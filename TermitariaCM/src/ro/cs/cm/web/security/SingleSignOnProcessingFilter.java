/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *  Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *   
 *  Termitaria is free software; you can redistribute it and/or 
 *  modify it under the terms of the GNU Affero General Public License 
 *  as published by the Free Software Foundation; either version 3 of 
 *  the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License 
 *  along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.cm.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class SingleSignOnProcessingFilter extends UsernamePasswordAuthenticationFilter {


    protected boolean requiresAuthentication(
        HttpServletRequest request, HttpServletResponse response){

            String userId = request.getParameter("username");
            String password = request.getParameter("password");

            System.out.println("=============================================");
            System.out.println("userId: " + userId);
            System.out.println("password: " + password);
            System.out.println("=============================================");

            if(userId != null || password != null){
                System.out.println("**********************************************");
                System.out.println("return 'logged in'");
                return false;
            } else {
                System.out.println("**********************************************");
                System.out.println("return 'null'");
                return true;
            }

        }

}

