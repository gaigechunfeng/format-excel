package com.wk.excel;

import com.wk.excel.entity.CustomerMasterData;
import com.wk.excel.entity.MaterialMasterData;
import com.wk.excel.entity.PhoneNameAddr;
import com.wk.excel.entity.Province;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Sheet;

import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 005689 on 2017/9/21.
 */
public class App implements Runnable {

    private static final Map<String, MaterialMasterData> MaterialMasterDataMap = new HashMap<>();
    private static final Map<String, CustomerMasterData> CustomerMasterDataMap = new HashMap<>();
    private static final Map<String, PhoneNameAddr> PhoneNameAddrMap = new HashMap<>();
    private static final Map<String, Province> ProvinceMap = new HashMap<>();
    private static final Map<String, String> OrderStatusMap = new HashMap<>();

    private static final JPanel PANEL = new JPanel(new FlowLayout());
    private static final JTextArea ERROR_AREA = createErrorArea();

    private static JTextArea createErrorArea() {
        JTextArea textArea = new JTextArea(50, 50);

//        textArea.setSize(966, 400);
        return textArea;
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(new App());
    }

    public void run() {

        JFrame frame = new JFrame("这个一个小工具");

        renderPanel();
        frame.add(PANEL);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(966, 557);
        frame.setVisible(true);
    }

    private void renderPanel() {

        PANEL.add(createUploadBtn());
        PANEL.add(ERROR_AREA, FlowLayout.CENTER);
    }


    private JButton createUploadBtn() {

        JButton button = new JButton("上传模板");
        button.setSize(200, 100);
        button.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("."));
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.setFileFilter(new FileNameExtensionFilter("只允许选择excel文件", "xls", "xlsx"));

                int retVal = fileChooser.showOpenDialog(PANEL);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    File selFile = fileChooser.getSelectedFile();
                    System.out.println("selected file is::" + selFile);

                    try {
                        parseTemplate(selFile);
                    } catch (Exception e1) {
                        error(e1);
                    }
                }
            }
        });

        return button;
    }

    private void error(Throwable e1) {
        e1.printStackTrace();
//        StringWriter sw = new StringWriter();
//        PrintWriter pw = new PrintWriter(sw);
//        e1.printStackTrace(pw);
        ERROR_AREA.append(e1.getMessage() + "\r\n");
        ERROR_AREA.requestFocus();
    }

    private void parseTemplate(File selFile) throws IOException, InvalidFormatException {


        Workbook workbook = new HSSFWorkbook(new FileInputStream(selFile));
//        Workbook workbook = new XSSFWorkbook(new FileInputStream(selFile));
//        Workbook workbook = new XSSFWorkbook(OPCPackage.open(selFile));
//        Workbook workbook = WorkbookFactory.create(selFile);
//

        exactMaterialMasterData(workbook.getSheet("Material master data"));
        exactCustomerMasterData(workbook.getSheet("Customer master data"));
        exactOrderStatusMap(workbook.getSheet("订单状态"));

        writeData(workbook.getSheet("order details"));

        write2File(workbook);

        close(workbook);

    }

    private void close(Workbook workbook) {
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void write2File(Workbook workbook) {

        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setFileFilter(new FileNameExtensionFilter("只允许保存为excel文件", "xls", "xlsx"));

        if (fileChooser.showOpenDialog(PANEL) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (FileOutputStream fos = new FileOutputStream(file)) {

                workbook.write(fos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeData(Sheet sheet) {

        writeHead(sheet);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            try {
                Row row = sheet.getRow(i);

                if (row == null) return;

                Cell cell = row.createCell(28);
                Cell cell2 = row.createCell(29);
                Cell cell3 = row.createCell(30);
                Cell cell4 = row.createCell(31);
                Cell cell5 = row.createCell(32);
                Cell cell6 = row.createCell(33);
                Cell cell7 = row.createCell(34);

                String proName = obj2String(getCellVal(row.getCell(24)));
                if (isStringBlank(proName)) throw new RuntimeException("【宝贝标题，Y】为空");
                MaterialMasterData materialMasterData = MaterialMasterDataMap.get(proName);
                if (materialMasterData == null) error("第" + (i + 1) + "行有问题，MaterialMasterData 未找到【" + proName + "】");

                String num = obj2String(getCellVal(row.getCell(25)));
                int numInt = 1;
                if (isStringBlank(num) || !isInteger(num)) {
                    error("第" + (i + 1) + "行有问题，【宝贝总数量】错误");
                } else {
                    numInt = Integer.parseInt(num);
                }

                String orderTime = obj2String(getCellVal(row.getCell(11)));
                if (isStringBlank(orderTime) || orderTime.length() < 9) throw new RuntimeException("【订单ID】为空或者长度小于9！");

                String phone = obj2String(getCellVal(row.getCell(23)));
                if (isStringBlank(phone)) throw new RuntimeException("【联系手机】为空！");
                CustomerMasterData customerMasterData = CustomerMasterDataMap.get(phone);
                if (customerMasterData == null) error("第" + (i + 1) + "行有问题，CustomerMasterDataMap 未找到【" + phone + "】");

                String orderStatus = obj2String(getCellVal(row.getCell(14)));
                if (isStringBlank(orderStatus)) throw new RuntimeException("【订单状态】为空！");
                String od = OrderStatusMap.get(orderStatus);
                if (od == null) error("第" + (i + 1) + "行有问题，OrderStatusMap 未找到【" + orderStatus + "】");


                cell.setCellValue(materialMasterData == null ? 0 : materialMasterData.getNetWeight() * numInt);
                cell2.setCellValue(materialMasterData == null ? 0 : materialMasterData.getGrossWeight() * numInt);
                cell3.setCellValue(orderTime.substring(1, 9));
                cell4.setCellValue(orderTime.substring(1, 7));
                cell5.setCellValue(customerMasterData == null ? "" : customerMasterData.getNo());
                cell6.setCellValue(customerMasterData == null ? "" : customerMasterData.getType());
                cell7.setCellValue(od == null ? "" : od);
            } catch (Exception e) {
                error(new RuntimeException("第" + (i + 1) + "行有问题，" + e.getMessage()));
            }
        }
    }

    private boolean isInteger(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void error(String s) {
        ERROR_AREA.append(s + "\r\n");
    }

    private void writeHead(Sheet sheet) {

        Row row = sheet.getRow(0);
        if (row == null) throw new RuntimeException("未找到表头");

        Cell cell = row.createCell(28);
        Cell cell2 = row.createCell(29);
        Cell cell3 = row.createCell(30);
        Cell cell4 = row.createCell(31);
        Cell cell5 = row.createCell(32);
        Cell cell6 = row.createCell(33);
        Cell cell7 = row.createCell(34);

        cell.setCellValue("订单净重(kg)");
        cell2.setCellValue("订单毛重(kg)");
        cell3.setCellValue("订单月份");
        cell4.setCellValue("订单日期");
        cell5.setCellValue("客户编号");
        cell6.setCellValue("客户分类");
        cell7.setCellValue("订单是否有效");
    }

    private void exactOrderStatusMap(Sheet sheet) {

        OrderStatusMap.clear();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if (row == null) break;
            try {
                String s1 = obj2String(getCellVal(row.getCell(0)));
                String s2 = obj2String(getCellVal(row.getCell(1)));

                OrderStatusMap.put(s1, s2);
            } catch (Exception e) {
                throw new RuntimeException("exactOrderStatusMap error!{" + e.getMessage() + "}{" + i + "}");
            }
        }

        System.out.println("OrderStatusMap::" + OrderStatusMap);
    }

    private void exactCustomerMasterData(Sheet sheet) {

        exactPhoneNameAddrMap();
        exactProvinceMap();

        CustomerMasterDataMap.clear();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if (row == null) {

//                System.out.println("第" + (i + 1) + "行有问题::Row=" + row + "---Cell0=" + row.getCell(0) + "---Cell1=" + row.getCell(1) + "----Cell6=" + row.getCell(6));
                break;
            }
            try {
                String s1 = obj2String(getCellVal(row.getCell(0)));
                String s2 = obj2String(getCellVal(row.getCell(1)));
                String s3 = obj2String(getCellVal(row.getCell(6)));

                CustomerMasterData customerMasterData = new CustomerMasterData();
                customerMasterData.setPhone(s1);
                customerMasterData.setNo(s2);
                customerMasterData.setType(s3);

                CustomerMasterDataMap.put(s1, customerMasterData);
            } catch (Exception e) {
                throw new RuntimeException("exactCustomerMasterData error!{" + e.getMessage() + "}{" + i + "}");
            }
        }

        System.out.println("CustomerMasterDataMap::" + CustomerMasterDataMap);
    }

    private Object getCellVal(Cell cell) {

        if (cell == null) return null;
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                return double2String(cell.getNumericCellValue());
            default:
                return cell.getStringCellValue().trim();
        }
    }

    private String double2String(double d) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(200);
        return df.format(d);
    }

    private String obj2String(Object d) {

        if (d == null) return "";
        if (d.getClass() == double.class) {
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(200);
            return df.format((double) d);
        } else {
            return d.toString().trim();
        }
    }

    private void exactProvinceMap() {

    }

    private void exactPhoneNameAddrMap() {

    }

    private void exactMaterialMasterData(Sheet sheet) {

        MaterialMasterDataMap.clear();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if (row == null || row.getCell(0) == null || row.getCell(1) == null || row.getCell(2) == null) break;
            try {
                String s1 = row.getCell(0).getStringCellValue();

                MaterialMasterData materialMasterData = new MaterialMasterData();
                materialMasterData.setTitle(s1);
                materialMasterData.setNetWeight((float) row.getCell(1).getNumericCellValue());
                materialMasterData.setGrossWeight((float) row.getCell(2).getNumericCellValue());

                MaterialMasterDataMap.put(s1, materialMasterData);
            } catch (Exception e) {
                throw new RuntimeException("exactMaterialMasterData error!{" + e.getMessage() + "}{" + i + "}");
            }
        }

        System.out.println("MaterialMasterDataMap::" + MaterialMasterDataMap);
    }

    private static boolean isStringBlank(String s2) {
        return s2 == null || "".equals(s2.trim());
    }

//    private static String getRowCellString(Row row, int i) {
//        Cell cell = row.getCell(i);
//        if (cell == null) {
//            return null;
//        } else {
//            switch (cell.getCellTypeEnum()) {
//                case NUMERIC:
//                    return String.valueOf(cell.getNumericCellValue());
//                default:
//                    return cell.getStringCellValue();
//            }
//        }
//    }
}
