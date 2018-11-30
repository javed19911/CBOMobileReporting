package utils;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.Environment;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import utils_new.Custom_Variables_And_Method;


public class MyConnection {

    Context context;

    public MyConnection(Context c) {
        context = c;
       /* sharedpreferences = context.getSharedPreferences("MyPrefrence", Context.MODE_PRIVATE);*/

    }


    public void create_xml(LinkedHashMap<String, ArrayList<String>> data,String title){

        ArrayList<String> getKeyList=new ArrayList<>();
        for (String key : data.keySet()) {
            getKeyList.add(key);
            //count.add(get_count(key));
        }

        String Fnamexls="Report"  + ".xls";
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File (sdCard.getAbsolutePath() + "/cbo");
        directory.mkdirs();
        File file = new File(directory, Fnamexls);
        file.deleteOnExit();

        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook;
        try {
            int a = 1;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //workbook.createSheet("Report", 0);

            WritableSheet sheet = workbook.createSheet("First Sheet", 0);
            sheet.mergeCells(0, 0, getKeyList.size()-1, 0);
            sheet.mergeCells(0, 1, getKeyList.size()-1, 1);
            sheet.addCell(new Label(0, 0, Custom_Variables_And_Method.COMPANY_NAME,createFormatCellStatus(true)));
            sheet.addCell(new Label(0, 1, title+" from "+Custom_Variables_And_Method.extraFrom+" to "+Custom_Variables_And_Method.extraTo,createFormatCellStatus(true)));
            for(int i=0;i<getKeyList.size();i++){
                sheet.addCell(new Label(i, 3, getKeyList.get(i),createFormatCellStatus(true)));
                String key=getKeyList.get(i);
               for(int j=0;j<data.get(key).size();j++){
                    sheet.addCell(new Label(i, j+4, data.get(key).get(j),createFormatCellStatus(false)));
                }
            }

        /*    Label label = new Label(0, 2, "SECOND");
            Label label1 = new Label(0,1,"first");
            Label label0 = new Label(0,0,"HEADING");
            Label label3 = new Label(1,0,"Heading2");
            Label label4 = new Label(1,1,String.valueOf(a));
            try {
                sheet.addCell(label);
                sheet.addCell(label1);
                sheet.addCell(label0);
                sheet.addCell(label4);
                sheet.addCell(label3);
            } catch (RowsExceededException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (WriteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

*/
            workbook.write();
            try {
                workbook.close();

                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                //File fileWithinMyDir = new File(file);

                if(file.exists()) {
                    intentShareFile.setType("application/pdf");
                    intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file));

                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                            "Sharing File...");
                    intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

                    context.startActivity(Intent.createChooser(intentShareFile, "Share File"));
                }

            } catch (WriteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //createExcel(excelSheet);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    public WritableCellFormat createFormatCellStatus(boolean b) throws WriteException{
        Colour colour = (b ) ? Colour.WHITE : Colour.AUTOMATIC;
        WritableFont wfontStatus = new WritableFont(WritableFont.createFont("Arial"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, colour);
        WritableCellFormat fCellstatus = new WritableCellFormat(wfontStatus);
        Colour colour1= new Colour(10000, "1", 255, 0, 0) {
        };

        fCellstatus.setWrap(true);
        if (b) {
            fCellstatus.setBackground(Colour.PALE_BLUE);
        }
        fCellstatus.setAlignment(jxl.format.Alignment.CENTRE);
        fCellstatus.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        fCellstatus.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.MEDIUM, Colour.PALE_BLUE);
        return fCellstatus;
    }


}



