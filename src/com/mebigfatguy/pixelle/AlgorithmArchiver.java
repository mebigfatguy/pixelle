/*
 * pixelle - Graphics algorithmic editor
 * Copyright (C) 2008-2019 Dave Brosius
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.mebigfatguy.pixelle;

import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.mebigfatguy.pixelle.utils.XMLEncoder;

public class AlgorithmArchiver {

    private static final String SYSTEM_ALGO_XML_PATH = "/com/mebigfatguy/pixelle/resources/algorithms.xml";
    private static final String SYSTEM_ALGO_XSD_PATH = "/com/mebigfatguy/pixelle/resources/algorithms.xsd";
    public static final String TYPE = "type";
    public static final String GROUP = "group";
    public static final String CURRENT = "current_";
    public static final String ALGORITHM = "algorithm";
    public static final String COMPONENT = "component";
    public static final String NAME = "name";
    public static final String PIXELLE = ".mebigfatguy/pixelle";
    public static final String ALGORITHMS_FILE = "algorithms.xml";

    private static InputStream XSD_STREAM;

    static {
        try (InputStream is = AlgorithmArchiver.class.getResourceAsStream(SYSTEM_ALGO_XSD_PATH)) {
            if (is != null) {
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    byte[] data = new byte[1024];
                    int len = is.read(data);
                    while (len >= 0) {
                        baos.write(data, 0, len);
                        len = is.read(data);
                    }
                    XSD_STREAM = new ByteArrayInputStream(baos.toByteArray());
                }
            }
        } catch (IOException e) {
            // ignore
        }
    }

    private static AlgorithmArchiver ARCHIVER = new AlgorithmArchiver();

    private final Map<ImageType, Map<String, Map<String, Map<PixelleComponent, String>>>> systemAlgorithms;
    private final Map<ImageType, Map<String, Map<String, Map<PixelleComponent, String>>>> userAlgorithms;

    private AlgorithmArchiver() {
        systemAlgorithms = new EnumMap<>(ImageType.class);
        userAlgorithms = new EnumMap<>(ImageType.class);
        loadSystemAlgorithms();
        loadUserAlgorithms();
    }

    public static AlgorithmArchiver getArchiver() {
        return ARCHIVER;
    }

    public JPopupMenu getAlgorithmDisplayPopup(ImageType imageType, ActionListener l) {
        JPopupMenu m = new JPopupMenu(PixelleBundle.getString(PixelleBundle.PIXEL_ALGORITHM));
        populateMenuAlgorithms(m, systemAlgorithms.get(imageType), l);
        populateMenuAlgorithms(m, userAlgorithms.get(imageType), l);

        return m;
    }

    private static void populateMenuAlgorithms(JPopupMenu menu, Map<String, Map<String, Map<PixelleComponent, String>>> algorithms, ActionListener l) {
        if (algorithms != null) {
            for (final Map.Entry<String, Map<String, Map<PixelleComponent, String>>> entry : algorithms.entrySet()) {
                String groupName = entry.getKey();
                if (!CURRENT.equals(groupName)) {
                    JMenu group = new JMenu(groupName);
                    menu.add(group);
                    for (final String algos : entry.getValue().keySet()) {
                        JMenuItem algoItem = new JMenuItem(algos);
                        algoItem.putClientProperty(NAME, groupName);
                        algoItem.addActionListener(l);
                        group.add(algoItem);
                    }
                }
            }
        }
    }

    public Map<PixelleComponent, String> getAlgorithm(ImageType imageType, String groupName, String algorithmName) {
        Map<String, Map<String, Map<PixelleComponent, String>>> type = systemAlgorithms.get(imageType);
        if (type != null) {
            Map<String, Map<PixelleComponent, String>> group = type.get(groupName);
            if (group != null) {
                Map<PixelleComponent, String> algo = group.get(algorithmName);
                if (algo != null) {
                    return algo;
                }
            }
        }

        type = userAlgorithms.get(imageType);
        if (type == null) {
            throw new IllegalArgumentException("Unknown type name " + imageType.name());
        }

        Map<String, Map<PixelleComponent, String>> group = type.get(groupName);
        if (group == null) {
            throw new IllegalArgumentException("Unknown group name " + groupName);
        }

        Map<PixelleComponent, String> algo = group.get(algorithmName);
        if (algo == null) {
            throw new IllegalArgumentException("Unknown algorithm name " + algorithmName);
        }

        return algo;

    }

    public String[] getUserGroups(ImageType imageType) {
        Set<String> groups = new TreeSet<>(userAlgorithms.get(imageType).keySet());
        groups.remove(CURRENT);
        return groups.toArray(new String[groups.size()]);
    }

    public void addAlgorithm(ImageType imageType, String groupName, String algorithmName, Map<PixelleComponent, String> algorithm) {
        Map<String, Map<String, Map<PixelleComponent, String>>> type = userAlgorithms.get(imageType);
        if (type == null) {
            type = new HashMap<>();
            userAlgorithms.put(imageType, type);
        }

        Map<String, Map<PixelleComponent, String>> group = type.get(groupName);
        if (group == null) {
            group = new HashMap<>();
            type.put(groupName, group);
        }

        group.put(algorithmName, new EnumMap<>(algorithm));
    }

    public void setCurrent(ImageType imageType, Map<PixelleComponent, String> algorithm) {
        addAlgorithm(imageType, AlgorithmArchiver.CURRENT, AlgorithmArchiver.CURRENT, algorithm);
    }

    public Map<PixelleComponent, String> getCurrent(ImageType imageType) {
        return getAlgorithm(imageType, AlgorithmArchiver.CURRENT, AlgorithmArchiver.CURRENT);
    }

    public void removeAlgorithm(ImageType imageType, String group, String name) {
        Map<String, Map<String, Map<PixelleComponent, String>>> type = userAlgorithms.get(imageType);
        if (type != null) {
            Map<String, Map<PixelleComponent, String>> algoGroup = type.get(group);
            if (algoGroup != null) {
                algoGroup.remove(name);
            }
        }
    }

    public void save() {
        try {
            Path pixelleDir = Paths.get(System.getProperty("user.home"), PIXELLE);
            Files.createDirectories(pixelleDir);

            Path algoFile = pixelleDir.resolve(ALGORITHMS_FILE);

            try (OutputStream xmlOut = new BufferedOutputStream(Files.newOutputStream(algoFile))) {
                writeAlgorithms(xmlOut, userAlgorithms);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadSystemAlgorithms() {
        try (InputStream xmlIs = AlgorithmArchiver.class.getResourceAsStream(SYSTEM_ALGO_XML_PATH)) {

            parseAlgorithms(xmlIs, systemAlgorithms);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void loadUserAlgorithms() {
        try {
            Path pixelleDir = Paths.get(System.getProperty("user.home"), PIXELLE);
            Files.createDirectories(pixelleDir);

            Path algoFile = pixelleDir.resolve(ALGORITHMS_FILE);
            if (Files.exists(algoFile) && !Files.isDirectory(algoFile)) {

                try (InputStream xmlIs = new BufferedInputStream(Files.newInputStream(algoFile))) {

                    parseAlgorithms(xmlIs, userAlgorithms);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    private static void writeAlgorithms(OutputStream is, Map<ImageType, Map<String, Map<String, Map<PixelleComponent, String>>>> algorithms) {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(is))) {

            pw.println("<algorithms xmlns='http://pixelle.mebigfatguy.com/" + Version.getVersion() + "'");
            pw.println("            xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'");
            pw.println("            xsi:schemaLocation='/com/mebigfatguy/pixelle/resources/algorithms.xsd'>");

            for (Map.Entry<ImageType, Map<String, Map<String, Map<PixelleComponent, String>>>> type : algorithms.entrySet()) {
                pw.println("    <type name='" + type.getKey() + "'>");
                for (Map.Entry<String, Map<String, Map<PixelleComponent, String>>> group : type.getValue().entrySet()) {
                    pw.println("        <group name='" + group.getKey() + "'>");

                    for (Map.Entry<String, Map<PixelleComponent, String>> algorithm : group.getValue().entrySet()) {
                        pw.println("            <algorithm name='" + algorithm.getKey() + "'>");

                        for (Map.Entry<PixelleComponent, String> component : algorithm.getValue().entrySet()) {
                            pw.println("                <component name='" + component.getKey().name().toLowerCase() + "'>");
                            pw.println("                    " + XMLEncoder.xmlEncode(component.getValue()));
                            pw.println("                </component>");
                        }
                        pw.println("            </algorithm>");
                    }
                    pw.println("        </group>");
                }
                pw.println("    </type>");
            }
            pw.println("</algorithms>");
            pw.flush();
        }
    }

    private static void parseAlgorithms(InputStream xmlIs, final Map<ImageType, Map<String, Map<String, Map<PixelleComponent, String>>>> algorithms)
            throws IOException, SAXException {
        try {
            if (XSD_STREAM != null) {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                spf.setValidating(true);
                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                spf.setSchema(schemaFactory.newSchema(new StreamSource(XSD_STREAM)));
            }
            XMLReader r = XMLReaderFactory.createXMLReader();
            r.setContentHandler(new DefaultHandler() {
                Map<String, Map<String, Map<PixelleComponent, String>>> currentType = null;
                Map<String, Map<PixelleComponent, String>> currentGroup = null;
                Map<PixelleComponent, String> currentAlgorithm = null;
                String currentComponentName = null;
                StringBuilder algorithmText = null;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes atts) {
                    if (TYPE.equals(localName)) {
                        currentType = new HashMap<>();
                        algorithms.put(ImageType.valueOf(atts.getValue(NAME)), currentType);
                    } else if (GROUP.equals(localName)) {
                        currentGroup = new HashMap<>();
                        currentType.put(atts.getValue(NAME), currentGroup);
                    } else if (ALGORITHM.equals(localName)) {
                        currentAlgorithm = new EnumMap<>(PixelleComponent.class);
                        currentGroup.put(atts.getValue(NAME), currentAlgorithm);
                    } else if (COMPONENT.equals(localName)) {
                        currentComponentName = atts.getValue(NAME);
                        algorithmText = new StringBuilder();
                    }
                }

                @Override
                public void characters(char[] c, int start, int offset) {
                    if (currentComponentName != null) {
                        algorithmText.append(c, start, offset);
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) {
                    if (COMPONENT.equals(localName)) {
                        PixelleComponent pc = PixelleComponent.valueOf(currentComponentName.toUpperCase());
                        currentAlgorithm.put(pc, algorithmText.toString().trim());
                        algorithmText = null;
                        currentComponentName = null;
                    } else if (ALGORITHM.equals(localName)) {
                        currentAlgorithm = null;
                    } else if (GROUP.equals(localName)) {
                        currentGroup = null;
                    } else if (TYPE.equals(localName)) {
                        currentType = null;
                    }
                }
            });
            r.parse(new InputSource(xmlIs));
        } finally {
            if (XSD_STREAM != null) {
                XSD_STREAM.reset();
            }
        }
    }
}
