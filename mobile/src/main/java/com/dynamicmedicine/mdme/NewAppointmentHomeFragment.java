package com.dynamicmedicine.mdme;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewAppointmentHomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewAppointmentHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewAppointmentHomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner mClinicSpinner;
    private Context mContext;
    private Button mLoadClinicButton;

    private OnFragmentInteractionListener mListener;
    private List<Clinic> mClinics;


    public NewAppointmentHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewAppointmentHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewAppointmentHomeFragment newInstance(String param1, String param2) {
        NewAppointmentHomeFragment fragment = new NewAppointmentHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_appointment_home, container, false);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ClinicDatabaseHandler cdb = new ClinicDatabaseHandler(mContext);
        mClinics = cdb.getAllClinics();
        cdb.close();
        attachViewElements();
//        Spinner spinner = (Spinner) getView().findViewById(R.id.new_appointment_clinic_spinner);
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        this.mContext = context;
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void loadClinicInfo() {
        CardView cardview = (CardView) getView().findViewById(R.id.new_appointment_clinic_card_view);
        //TODO SEE http://stackoverflow.com/questions/18742274/how-to-animate-the-width-and-height-of-a-layout FOR ANIAMTION

//        cardview.animate();
        ViewGroup.LayoutParams lp = cardview.getLayoutParams();
//        ResizeAnimation a = new ResizeAnimation(cardview);
//        a.setDuration(500);
//        a.setParams(lp.height, 1400);
//        cardview.startAnimation(a);
        lp.height = 1400;
        cardview.setLayoutParams(lp);
        Clinic clinic = (Clinic) mClinicSpinner.getSelectedItem();

        NewAppointmentClinicFragment clinicFragment = NewAppointmentClinicFragment.newInstance(clinic);

        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.new_appointment_clinic_holder, clinicFragment);
        transaction.commit();
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.homeNavFragment, clinicFragment)
//                .addToBackStack(null)
//                .commit();
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void attachViewElements() {
        mClinicSpinner = (Spinner) getView().findViewById(R.id.new_appointment_clinic_spinner);
        Clinic[] clinicsArray = new Clinic[mClinics.size()];
        mClinics.toArray(clinicsArray);
        ArrayAdapter<Clinic> adapter = new ArrayAdapter<Clinic>(mContext,
                android.R.layout.simple_spinner_item, mClinics);
        mClinicSpinner.setAdapter(adapter);

        mLoadClinicButton = (Button) getView().findViewById(R.id.new_appointment_load_clinic_button);
        mLoadClinicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadClinicInfo();
            }
        });

    }
}
