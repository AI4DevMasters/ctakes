/*
 * Copyright: (c) 2009   Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
package edu.mayo.bmi.uima.core.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

/**
 * Implementation for StringIntegerMapResource interface.  
 * 
 * @author Mayo Clinic
 */
public class StringIntegerMapResourceImpl
        implements StringIntegerMapResource, SharedResourceObject
{
    // LOG4J logger based on class name
    private Logger iv_logger = Logger.getLogger(getClass().getName());

    private final String DELIMITER = "|";

    Map iv_map = new HashMap();

    /**
     *  Loads data from a file.
     */
    public void load(DataResource dr) throws ResourceInitializationException
    {
        iv_logger.info("Loading resource: "+dr.getUrl());
        try
        {
            InputStream inStream = dr.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    inStream));
            int lineCount = 0;
            String line = br.readLine();
            while (line != null)
            {
                lineCount++;

                StringTokenizer st = new StringTokenizer(line, DELIMITER);
                if (st.countTokens() == 2)
                {
                    String key = st.nextToken();
                    try
                    {
                        Integer value = new Integer(st.nextToken());
                        iv_map.put(key, value);
                    }
                    catch (NumberFormatException nfe)
                    {
                        iv_logger.warn("Invalid resource line, expected integer: " + line);
                    }
                }
                else
                {
                    iv_logger.warn("Invalid resource line, expected 2 tokens only.");
                }

                line = br.readLine();
            }
            br.close();

            iv_logger.info("Loaded resource, # lines=" + lineCount);
        }
        catch (IOException ioe)
        {
            throw new ResourceInitializationException(ioe);
        }
    }

    /**
     * Gets a map of the String/Integer values.
     */
    public Map getMap()
    {
        return iv_map;
    }
}