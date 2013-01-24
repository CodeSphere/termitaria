/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *    Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *     
 *    Termitaria is free software; you can redistribute it and/or 
 *    modify it under the terms of the GNU Affero General Public License 
 *    as published by the Free Software Foundation; either version 3 of 
 *    the License, or (at your option) any later version.
 *    
 *    This program is distributed in the hope that it will be useful, 
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *    GNU Affero General Public License for more details.
 *    
 *    You should have received a copy of the GNU Affero General Public License 
 *    along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.ts.common;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author dan.damian
 *
 * Folosita pentru afisarea informatiilor preluate de pe request/sesiune
 */
public class TextTable {

    public static final byte ALIGN_CENTER = 0;
    public static final byte ALIGN_RIGHT = 1;
    public static final byte ALIGN_LEFT = 2;
    private static final String START_OF_LINE = "  ";
    private static final char END_OF_LINE = '\n';
    private static final char VERTICAL_BORDER = '|';
    private static final char HORIZONTAL_BORDER_UP = '-';
    private static final char HORIZONTAL_BORDER_DOWN = '-';
    private static String ELEMENT_SEPARATOR = "#";
    private static final int MAX_NO_OF_ROWS_ALLOWED = 50;

    private String[] header;
    private ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
    private ArrayList<Byte> rowAlign = new ArrayList<Byte>();
    private byte currentRowNo = 0;
    private int collNo = 0;
    private byte headerAlign = -1;
    
    private Log logger = LogFactory.getLog(getClass());

    public TextTable(String theHeader, byte align) {
        this(theHeader, null, align);
    }

    public TextTable(String theHeader, String elementSeparator, byte align) {
        if (elementSeparator != null) {
            ELEMENT_SEPARATOR = elementSeparator;
        }
        String[] st = theHeader.split(ELEMENT_SEPARATOR);
        headerAlign = align;
        header = new String[st.length];
        this.collNo = header.length;
        // populam headerul
        int i = 0;
        StringBuffer sb = new StringBuffer();
        for(int j=0;j<st.length;j++){
            sb.delete(0, sb.length());
            sb.append(" ");
            sb.append((String) st[j]);
            sb.append(" ");
            header[i++] = sb.toString();
        }
    }

    public void addRow(String theRow) throws Exception {
        addRow(theRow, ALIGN_LEFT);
    }

    public void addRow(String theRow, byte aRowAlign) {
        // dk am speficat ca nu avem nici o linie (doar headerul)
        // nu face nimic
        // adaug inca o linie la matricea tabel
        // log.debug("addRow() -> CurrentRowNo begin = " + currentRowNo);
        if (currentRowNo > MAX_NO_OF_ROWS_ALLOWED) {
            logger.info("No of allowed cols ".concat(String.valueOf(MAX_NO_OF_ROWS_ALLOWED)));
        }
        ArrayList<String> row = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(theRow, ELEMENT_SEPARATOR);
        rowAlign.add(new Byte(aRowAlign));
        int j = 0;
        boolean amAvutCaracterEndLine = false;
        StringBuffer sb = new StringBuffer();
        while (st.hasMoreElements()) {
            // punem un spatiu in stanga si in dreapta continutului
            sb.delete(0, sb.length()); // resetez StringBufferul
            sb.append(" ");
            String s = (String) st.nextElement();
            if (s.indexOf('\n') != -1) {
                s = s.replace('\n', '?');
                amAvutCaracterEndLine = true;
            }
            sb.append(s);
            sb.append(" ");
            row.add(sb.toString());
        }
        // in cazul in care o linie nu este completa (ca numar de coloane),
        // completez
        if (j < header.length) {
            for (int i = j; i < header.length; row.add(" "), i++) {
                ;
            }
        }
        if (amAvutCaracterEndLine) {
            row.add(" (? = \\n)");
        }
        table.add(row);
        // incrementez variabila statica currentRow pentru
        // a stii nr. de ordine al urmatoarei linii adaugate
        currentRowNo++;
        // log.debug("addRow() -> CurrentRowNo end = " + currentRowNo);
    }

    private int[] getMaxPerColls() {
        int[] max = new int[collNo];
        for (int i = 0; i < collNo; max[i] = header[i++].length());
        // dk am specificat ca avem doar headerul, fara nici o linie 'under'
        // calculeaza maximul doar din header
        if (currentRowNo == 0) {
            return max;
        }
        // dk nu calculam maximul din tot tabelul
        // log.debug("Avem " + table.size() + " linii");
        // log.debug("CurrentRowNo = " + currentRowNo);
        for (int i = 0; i < collNo; i++) { // pe COLOANE
            for (int j = 0; j < currentRowNo; j++) { // pe LINII
                if (max[i] < ((String) ((ArrayList<String>) table.get(j)).get(i)).length()) {
                    max[i] = ((String) ((ArrayList<String>) table.get(j)).get(i)).length();
                }
            }
        }
        return max;
    }

    public String getTable() {
        // buferul in care memorez tabelul
        StringBuffer sb = new StringBuffer();
        sb.append(END_OF_LINE);
        // lungimea maxima a unui cuvant pe fiecare coloana
        int[] max = getMaxPerColls();
        // latimea tablelului
        int L = (byte) (collNo + 1);
        for (int i = 0; i < collNo; L += max[i++]);
        sb.append(START_OF_LINE);
        for (int i = 0; i < L; sb.append(HORIZONTAL_BORDER_UP), i++);
        sb.append(END_OF_LINE);
        // adauga headerul
        sb.append(START_OF_LINE);
        sb.append(VERTICAL_BORDER);
        for (int i = 0; i < collNo; sb.append(alignGeneral(header[i], max[i], headerAlign)), sb.append(VERTICAL_BORDER), i++);
        sb.append(END_OF_LINE);
        sb.append(START_OF_LINE);
        for (int i = 0; i < L; sb.append(HORIZONTAL_BORDER_DOWN), i++);
        sb.append(END_OF_LINE);
        // adauga tabelul propriu zis
        if (currentRowNo > 0) {
            for (int i = 0; i < currentRowNo; i++) {// pe LINII
                sb.append(START_OF_LINE);
                sb.append(VERTICAL_BORDER);
                for (int j = 0; j < collNo; j++) {// pe COLOANE
                    sb.append(alignGeneral(((String) ((ArrayList<String>) table.get(i)).get(j)), max[j], ((Byte) rowAlign.get(i)).byteValue()));
                    sb.append(VERTICAL_BORDER);
                }
                sb.append(END_OF_LINE);
            }
            sb.append(START_OF_LINE);
            for (int i = 0; i < L; sb.append(HORIZONTAL_BORDER_DOWN), i++);
        }
        return sb.toString();
    }

    private String alignGeneral(String s, int peCat, byte alignment) {
        switch (alignment) {
        case ALIGN_CENTER:
            return alignCenter(s, peCat);
        case ALIGN_LEFT:
            return alignLeft(s, peCat);
        case ALIGN_RIGHT:
            return alignRight(s, peCat);
        }
        return null;
    }

    private String alignRight(String s, int peCat) {
        StringBuffer sb = new StringBuffer();
        if (s.length() < peCat) {
            for (int i = 0; i < (peCat - s.length()); sb.append(" "), i++);
        }
        sb.append(s);
        return sb.toString();
    }

    private String alignLeft(String s, int peCat) {
        StringBuffer sb = new StringBuffer();
        sb.append(s);
        if (s.length() < peCat) {
            for (int i = 0; i < (peCat - s.length()); sb.append(" "), i++);        }
        return sb.toString();
    }

    private String alignCenter(String s, int peCat) {
        StringBuffer sb = new StringBuffer();
        if (s.length() < peCat) {
            int freeSpace = peCat - s.length();
            int leftSpaces = freeSpace / 2;
            int rightSpaces = 0;
            if (freeSpace % 2 == 0) {
                rightSpaces = leftSpaces;
            } else {
                rightSpaces = leftSpaces + 1;
            }
            for (int i = 0; i < leftSpaces; sb.append(" "), i++);
            sb.append(s);
            for (int i = 0; i < rightSpaces; sb.append(" "), i++);
        } else {
            sb.append(s);
        }
        return sb.toString();
    }

    public static String showOnlyName(String s) {
        return (s.lastIndexOf('.') != -1) ? s.substring(s.lastIndexOf('.') + 1, s.length()) : s;
    }
}

