package com.example.batchprocessing;

import org.apache.commons.net.util.SubnetUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SuperGrep {
    public static SuperGrep grep(File dataFile, File matchFile, boolean fullGrep) throws Exception {
        List<String> matchLines = readLines(matchFile);
        return SuperGrep.grep(dataFile, matchLines, fullGrep);
    }

    private static List<String> readLines(File file) throws Exception {
        List<String> lines = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            //System.out.println(line);
            lines.add(line);
        }
        reader.close();

        return lines;
    }
    
    public static SuperGrep grep(File dataFile, List<String> lines, boolean fullGrep) throws Exception {
        for (String line : lines) {
            System.out.println(line);

            int lineNo = 0;
            boolean match = false;

            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            try {
                String d;
                while ((d = reader.readLine()) != null) {
                    lineNo++;
                    match = doMatchLine(d, line);
                    //System.out.println(String.format("doMatchLine %s, %s, %s", d, line, match));

                    if (match) {
                        System.out.println("   - # " + lineNo + ':' + d);

                        if (!fullGrep) {                        	
                        	break;
                        }
                    }
                }
            } catch (Exception e) {
                reader.close();
            }
        }

        SuperGrep sm = new SuperGrep();
        return sm;
    }

    private static final String IP_ADDRESS = "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})";
    private static final String SLASH_FORMAT = "((\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3}/\\d{1,3}))";
    private static final Pattern addressPattern = Pattern.compile(IP_ADDRESS);
    private static final Pattern cidrPattern = Pattern.compile(SLASH_FORMAT);

    private static boolean isIP(String target) {
        final Matcher matcher = addressPattern.matcher(target);
        return matcher.matches();
    }

    private static boolean isCID(String target) {
        final Matcher matcher = cidrPattern.matcher(target);
        return matcher.matches();
    }

    public static List<IPRange> parseIPLine(final String str) {
        Matcher m = cidrPattern.matcher(str);

        List<IPRange> matchers = new ArrayList<>();
        while (m.find()) {
            String group = m.group();
            // System.out.println(group );
            IPRange ipRange = getIPRange(group);
            if (ipRange != null) {
                matchers.add(ipRange);
            }
        }

        return matchers;
    }

    static boolean doMatchLine(String from, String to) {
        int pos = from.indexOf(to);
        if (pos > -1) {
            return true;
        }

        IPRange toIPRange = getIPRange(to);

        if (toIPRange != null) {
            List<IPRange> sourceIPRange = parseIPLine(from);

            boolean allIn = isAllIn(sourceIPRange, toIPRange);

            if (allIn) {
                return true;
            }
        }

        return false;
    }

    private static boolean isAllIn(List<IPRange> sourceIPRange, IPRange toIPRange) {
//        System.out.println("sourceIPRange: " + sourceIPRange.toString());
//        System.out.println("toIPRange: "+ toIPRange.toString());
        for (String toIP : toIPRange.getAddresses()) {
            boolean contains = isContains(sourceIPRange, toIP);
            if (!contains) {
                return false;
            }
        }
        return true;
    }

    private static boolean isContains(List<IPRange> sourceIPRange, String toIP) {
        for (IPRange sourceIP : sourceIPRange) {
//            System.out.println("sourceIP: " + sourceIP.toString());
//            System.out.println("toIP: " + toIP.toString());
            if (sourceIP.isInRange(toIP)) {
                return true;
            }
        }
        return false;
    }

    static IPRange getIPRange(String target) {
        try {
            if (isIP(target)) {
                return new IPRange(target);
            }

            if (isCID(target)) {
                SubnetUtils subnetUtils = new SubnetUtils(target);
                SubnetUtils.SubnetInfo info = subnetUtils.getInfo();
                if (info.getAddressCountLong() < Short.MAX_VALUE) {
                    return new IPRange(info.getAllAddresses());
                }

            }
        } catch (Exception e) {
            // new Exception(target, e).printStackTrace();
        }
        return null;
    }


}
