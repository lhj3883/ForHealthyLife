package com.team_comfortable.forhealthylife.ui.calendar.fragment;



import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.team_comfortable.forhealthylife.R;
import com.team_comfortable.forhealthylife.ui.calendar.adapter.CalendarAdapter;
import com.team_comfortable.forhealthylife.ui.calendar.util.Keys;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.firebase.ui.auth.ui.email.RegisterEmailFragment.TAG;


public class CalendarFragment extends Fragment
{
    public int mCenterPosition;
    public ArrayList<Object> mCalendarList = new ArrayList<>();
    public TextView textView;
    public RecyclerView recyclerView;
    private CalendarAdapter mAdapter;
    private StaggeredGridLayoutManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_calendar, container, false);
        initView(rootView);
        initSet();
        if (mCalendarList == null) {
            Log.w(TAG, "No Query, not initializing RecyclerView");
        }
        setRecycler();
        FloatingActionButton FloatingBtn = (FloatingActionButton) rootView.findViewById(R.id.btn_floating);
        FloatingBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                EnterFragment enterFragment = new EnterFragment();
                transaction.add(R.id.fragment_calendar, enterFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return rootView;
    }

    public void initView(View v)
    {
        textView = (TextView)v.findViewById(R.id.title);
        recyclerView = (RecyclerView)v.findViewById(R.id.calendar);
    }   

    public void initSet(){
        initCalendarList();
    }

    public void initCalendarList()
    {
        GregorianCalendar cal = new GregorianCalendar();
        setCalendarList(cal);
    }

     private void setRecycler()
     {
         manager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);
         mAdapter = new CalendarAdapter(mCalendarList);
         mAdapter.setCalendarList(mCalendarList);
         recyclerView.setLayoutManager(manager);
         recyclerView.setAdapter(mAdapter);
         mAdapter.setOnItemClicklistener(new CalendarAdapter.OnItemClickListener() {
             @Override
             public void onItemClick(String date) {
                 Log.i("tag", date);
                 FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                 ScheduleFragment scheduleFragment = new ScheduleFragment();
                 scheduleFragment.getDate(date);
                 transaction.add(R.id.fragment_calendar, scheduleFragment);
                 transaction.addToBackStack(this.getClass().getSimpleName());
                 transaction.commit();
             }
         });
         if (mCenterPosition >= 0) {
             recyclerView.scrollToPosition(mCenterPosition);
         }

     }

     public void setCalendarList(GregorianCalendar cal)
     {
         ArrayList<Object> calendarList = new ArrayList<>();
         for (int i = -300; i < 300; i++)
         {
             try
             {
                 GregorianCalendar calendar
                         = new GregorianCalendar(cal.get(Calendar.YEAR),
                         cal.get(Calendar.MONTH) + i, 1, 0, 0, 0);
                 if (i == 0) {
                     mCenterPosition = calendarList.size();
                 }
                 calendarList.add(calendar.getTimeInMillis());
                 int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                 int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                 for (int j = 0; j < dayOfWeek; j++) {
                     calendarList.add(Keys.EMPTY);
                 }
                 for (int j = 1; j <= max; j++) {
                     calendarList.add(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), j));
                 }
             }
             catch (Exception e) {
                 e.printStackTrace();
             }
         }
         mCalendarList = calendarList;
     }
}