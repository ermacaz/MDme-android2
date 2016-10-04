package com.dynamicmedicine.mdme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewAppointmentClinicFragment.OnNewAppointmentClinicFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewAppointmentClinicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewAppointmentClinicFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CLINIC = "clinic";

    // TODO: Rename and change types of parameters
    private Clinic mClinic;
    private final String TAG = "NewApptClinicFrgmt";
    private OnNewAppointmentClinicFragmentInteractionListener mListener;
    private Context mContext;
    private CalendarView mClinicCalendarView;
    private List<Procedure> mProcedures;
    private Spinner mProcedureSpinner;
    private Button mSelectDatesButton;
    private DateTime mAppointmentDate;
    private Procedure mAppointmentProcedure;


    public NewAppointmentClinicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param clinic clinic.
     * @return A new instance of fragment NewAppointmentClinicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewAppointmentClinicFragment newInstance(Clinic clinic) {
        NewAppointmentClinicFragment fragment = new NewAppointmentClinicFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLINIC, clinic);
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mClinic = (Clinic) getArguments().getSerializable(ARG_CLINIC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_appointment_clinic, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ProcedureDatabaseHandler pdb = new ProcedureDatabaseHandler(mContext);
        mProcedures = pdb.getAllProceduresForClinic(mClinic.getId());
        pdb.close();
        TextView name = (TextView) getView().findViewById(R.id.clinic_name);
        name.setText(mClinic.getName());
        mProcedureSpinner = (Spinner) getView().findViewById(R.id.new_appointment_procedure_spinner);
        Procedure[] proceduresArray = new Procedure[mProcedures.size()];
        mProcedures.toArray(proceduresArray);
        ArrayAdapter<Procedure> adapter = new ArrayAdapter<Procedure>(mContext,
                android.R.layout.simple_spinner_item, mProcedures);
        mProcedureSpinner.setAdapter(adapter);
        mSelectDatesButton = (Button) getView().findViewById(R.id.new_appointment_load_dates_button);
        mSelectDatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd =   DatePickerDialog.newInstance(
                        NewAppointmentClinicFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                Calendar cal;
                List<Calendar> weekends = new ArrayList<>();
                int weeks =16;
                for (int i = 0; i < (weeks * 7) ; i = i + 7) {
                    if (!mClinic.isOpenSunday()) {
                        cal = Calendar.getInstance();
                        cal.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - cal.get(Calendar.DAY_OF_WEEK) + 7 + i));
                        weekends.add(cal);
                    }
                    if (!mClinic.isOpenMonday()) {
                        cal = Calendar.getInstance();
                        cal.add(Calendar.DAY_OF_YEAR, (Calendar.MONDAY - cal.get(Calendar.DAY_OF_WEEK) + 7 + i));
                        weekends.add(cal);
                    }
                    if (!mClinic.isOpenTuesday()) {
                        cal = Calendar.getInstance();
                        cal.add(Calendar.DAY_OF_YEAR, (Calendar.TUESDAY - cal.get(Calendar.DAY_OF_WEEK) + 7 + i));
                        weekends.add(cal);
                    }
                    if (!mClinic.isOpenWednesday()) {
                        cal = Calendar.getInstance();
                        cal.add(Calendar.DAY_OF_YEAR, (Calendar.WEDNESDAY - cal.get(Calendar.DAY_OF_WEEK) + 7 + i));
                        weekends.add(cal);
                    }
                    if (!mClinic.isOpenThursday()) {
                        cal = Calendar.getInstance();
                        cal.add(Calendar.DAY_OF_YEAR, (Calendar.THURSDAY - cal.get(Calendar.DAY_OF_WEEK) + 7 + i));
                        weekends.add(cal);
                    }
                    if (!mClinic.isOpenFriday()) {
                        cal = Calendar.getInstance();
                        cal.add(Calendar.DAY_OF_YEAR, (Calendar.FRIDAY - cal.get(Calendar.DAY_OF_WEEK) + 7 + i));
                        weekends.add(cal);
                    }
                    if (!mClinic.isOpenSaturday()) {
                        cal = Calendar.getInstance();
                        cal.add(Calendar.DAY_OF_YEAR, (Calendar.SATURDAY - cal.get(Calendar.DAY_OF_WEEK) + 7 + i));
                        weekends.add(cal);
                    }

                }
                Calendar[] disabledDays = weekends.toArray(new Calendar[weekends.size()]);
                dpd.setDisabledDays(disabledDays);
                dpd.setMinDate(Calendar.getInstance());
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        initializeCalendar();
        super.onViewCreated(view, savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void initializeScheduling() {
        if (mListener != null) {
            mListener.processAppointmentInfo(mClinic, mAppointmentDate, mAppointmentProcedure);
        }
    }

    @Override
    public void onAttach(Context context) {
        this.mContext = context;
        super.onAttach(context);
        if (context instanceof OnNewAppointmentClinicFragmentInteractionListener) {
            mListener = (OnNewAppointmentClinicFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnNewAppointmentClinicFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mClinic = null;
    }

    public void initializeCalendar() {
//        mClinicCalendarView = (CalendarView)getView().findViewById(R.id.new_appointment_clinic_calendar);
//        mClinicCalendarView.setSelectedWeekBackgroundColor(getResources().getColor(R.color.MDme_primary));
//        mClinicCalendarView.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));
//        mClinicCalendarView.setSelectedDateVerticalBar(R.color.MDme_text_primary);
////sets the listener to be notified upon selected date change.
//        mClinicCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//                    //show the selected date as a toast
//            @Override
//            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
//                Toast.makeText(mContext, day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
//            }
//        });


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        //store date and procedure
        String date = year+"/"+(monthOfYear+1)+"/"+dayOfMonth;
        DateTimeFormatter dateDecoder = DateTimeFormat.forPattern("yyyy/MM/dd");
        mAppointmentDate = DateTime.parse(date, dateDecoder);
        mAppointmentProcedure = (Procedure) mProcedureSpinner.getSelectedItem();
        RequestParams params = new RequestParams();
        params.put("clinic_id", mClinic.getId());
        params.put("date", date);
        params.put("format", "as_scheduling_json");
        SharedPreferences preferences = mContext.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        String token = preferences.getString("ApiToken", "0");
        MdmeRestClient.get("/clinics/" + mClinic.getId() + "/appointments", params, token, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        //remove old appointments from db
                        AppointmentDatabaseHandler aDb = new AppointmentDatabaseHandler(mContext);
                        aDb.resetDb();
                        //save appointments
                        JSONArray appointments = response.getJSONObject("data").getJSONArray("appointments");
                        if (appointments.length() > 0) { //only returns clinics if they have updated
                            for (int i = 0; i < appointments.length(); i++) {
                                Appointment appointment = new Appointment();
                                JSONObject jsonAppointment = appointments.getJSONObject(i);
                                if (!jsonAppointment.isNull("id"))
                                    appointment.setId(jsonAppointment.getInt("id"));
                                if (!jsonAppointment.isNull("clinic_id"))
                                    appointment.setClinicId(jsonAppointment.getInt("clinic_id"));
                                if (!jsonAppointment.isNull("doctor_id"))
                                    appointment.setDoctorId(jsonAppointment.getInt("doctor_id"));
                                if (!jsonAppointment.isNull("start_time"))
                                    appointment.setStartTime(DateTime.parse(jsonAppointment.getString("start_time")));
                                if (!jsonAppointment.isNull("end_time"))
                                    appointment.setEndTime(DateTime.parse(jsonAppointment.getString("end_time")));
                                if (!jsonAppointment.isNull("duration"))
                                    appointment.setDuration(jsonAppointment.getInt("duration"));
                                if (!jsonAppointment.isNull("description"))
                                    appointment.setDescription(jsonAppointment.getString("description"));
                                if (!jsonAppointment.isNull("status"))
                                    appointment.setStatus(jsonAppointment.getString("status"));

                                if (aDb.appointmentExists(appointment.getId())) {
                                    aDb.updateAppointment(appointment);
                                } else {
                                    aDb.addAppointment(appointment);
                                }
                            }
                            aDb.close();
                        }
                        initializeScheduling();
                    }
                    else {
                        Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNewAppointmentClinicFragmentInteractionListener {
        void processAppointmentInfo(Clinic clinic, DateTime appointmentDate, Procedure appointmentProecedure);
    }
}
