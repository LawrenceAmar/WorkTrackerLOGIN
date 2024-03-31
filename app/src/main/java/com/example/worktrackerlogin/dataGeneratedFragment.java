package com.example.worktrackerlogin;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.worktrackerlogin.activity.activityDataClass;
import com.example.worktrackerlogin.pog.pogDataClass;
import com.example.worktrackerlogin.sales.salesDataClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class dataGeneratedFragment extends Fragment{

    ArrayList<activityDataClass> activitiesList = new ArrayList<>();
    ArrayList<salesDataClass> salesList = new ArrayList<>();
    ArrayList<pogDataClass> pogList = new ArrayList<>();
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceAct;
    DatabaseReference databaseReferenceSales;
    DatabaseReference databaseReferencePOG;
    DatabaseReference databaseReferenceUser;
    private String username;

    public dataGeneratedFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_data_generated, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_hhmmssSS", Locale.getDefault());
        String formattedDate = df.format(c);
        String filename1 = "/WorkTrackerData_" + formattedDate;
        String filename = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + filename1 + ".xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet actSheet = workbook.createSheet("Activities");
        XSSFSheet salesSheet = workbook.createSheet("Sales");
        XSSFSheet pogSheet = workbook.createSheet("Pog");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    username = Objects.requireNonNull(dataSnapshot.getKey());
                    Log.d("TEST", username);
                    String territory = dataSnapshot.child("territory").getValue(String.class);
                    databaseReferenceAct = FirebaseDatabase.getInstance().getReference("users").child(username).child("Activities");
                    databaseReferenceSales = FirebaseDatabase.getInstance().getReference("users").child(username).child("Sales");
                    databaseReferencePOG = FirebaseDatabase.getInstance().getReference("users").child(username).child("POG");

                    databaseReferenceAct.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                activityDataClass user = dataSnapshot.getValue(activityDataClass.class);
                                assert user != null;
                                activitiesList.add(user);
                            }
                            // Reverse the list to display most recent data first
                            Collections.reverse(activitiesList);
                            ArrayList<String[]> cellDataTable = new ArrayList<>();
                            try {
                                cellDataTable.add(new String[]{"Date", "Name of CSL", "Type of Activity", "Farmer Reach",
                                        "Crop", "Contact Person", "Contact Number"});

                                for (int i = 0; i < activitiesList.size(); i++) {
                                    activityDataClass activityData = activitiesList.get(i);
                                    cellDataTable.add(new String[]{activityData.getActDate(), activityData.getActName(),
                                            activityData.getActType(), String.valueOf(activityData.getActReach()),
                                            activityData.getActCrop(), activityData.getActContactPerson(),
                                            activityData.getActNumber()
                                    });
                                }

                                int rowNum = 0;
                                for (String[] activityData : cellDataTable) {
                                    Row row = actSheet.createRow(rowNum++);
                                    int colNum = 0;
                                    for (String cellData : activityData) {
                                        Cell cell = row.createCell(colNum++);
                                        cell.setCellValue(cellData);
                                    }
                                }

                                FileOutputStream outputStream = new FileOutputStream(filename);
                                workbook.write(outputStream);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                        }
                    });

                    databaseReferenceSales.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                salesDataClass user = dataSnapshot.getValue(salesDataClass.class);
                                assert user != null;
                                salesList.add(user);
                            }
                            // Reverse the list to display most recent data first
                            Collections.reverse(salesList);
                            ArrayList<String[]> cellDataTable = new ArrayList<>();
                            try {
                                cellDataTable.add(new String[]{"Date", "Territory", "Customer", "Product",
                                        "Order in pcs", "Price/pack", "Sales PHP"});

                                for (int i = 0; i < salesList.size(); i++) {
                                    salesDataClass salesData = salesList.get(i);
                                    cellDataTable.add(new String[]{salesData.getSalesDate(), territory,
                                            salesData.getCustomerName(), salesData.getProduct(),
                                            String.valueOf(salesData.getUnit()),
                                            String.valueOf(salesData.getPrice()),
                                            String.valueOf(salesData.getValue())
                                    });
                                }

                                int rowNum = 0;
                                for (String[] salesData : cellDataTable) {
                                    Row row = salesSheet.createRow(rowNum++);
                                    int colNum = 0;
                                    for (String cellData : salesData) {
                                        Cell cell = row.createCell(colNum++);
                                        cell.setCellValue(cellData);
                                    }
                                }

                                FileOutputStream outputStream = new FileOutputStream(filename);
                                workbook.write(outputStream);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                        }
                    });

                    databaseReferencePOG.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                pogDataClass user = dataSnapshot.getValue(pogDataClass.class);
                                assert user != null;
                                Log.d("TEST", user.getCustomer());
                                pogList.add(user);
                            }
                            // Reverse the list to display most recent data first
                            Collections.reverse(pogList);
                            ArrayList<String[]> cellDataTable = new ArrayList<>();
                            try {
                                cellDataTable.add(new String[]{"Year", "Month", "Territory", "Technology",
                                        "Brand", "Customer", "Beg. Inv. in units", "End Inv. in units",
                                        "POG in units", "Beg. Inv. in kgs", "End Inv. in kgs", "POG in kgs",
                                        "Beg. Inv. in ctn", "End Inv. in ctn", "POG in ctn",
                                        "Beg. Inv. in value", "End Inv. in value", "POG in value"
                                });

                                for (int i = 0; i < pogList.size(); i++) {
                                    pogDataClass pogData = pogList.get(i);
                                    cellDataTable.add(new String[]{pogData.getYear(), pogData.getMonth(),
                                            territory, pogData.getTech(), pogData.getBrand(), pogData.getCustomer(),
                                            String.valueOf(pogData.getBegInv()), String.valueOf(pogData.getEndInv()),
                                            String.valueOf(pogData.getPogUnits()), String.valueOf(pogData.getBegInvKgs()),
                                            String.valueOf(pogData.getEndInvKgs()), String.valueOf(pogData.getPogKgs()),
                                            String.valueOf(pogData.getBegInvCtn()), String.valueOf(pogData.getEndInvCtn()),
                                            String.valueOf(pogData.getPogCtn()), String.valueOf(pogData.getBegInvVal()),
                                            String.valueOf(pogData.getEndInvVal()), String.valueOf(pogData.getPogVal())
                                    });
                                }

                                int rowNum = 0;
                                for (String[] pogData : cellDataTable) {
                                    Row row = pogSheet.createRow(rowNum++);
                                    int colNum = 0;
                                    for (String cellData : pogData) {
                                        Cell cell = row.createCell(colNum++);
                                        cell.setCellValue(cellData);
                                    }
                                }

                                FileOutputStream outputStream = new FileOutputStream(filename);
                                workbook.write(outputStream);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
        return view;
    }
}
