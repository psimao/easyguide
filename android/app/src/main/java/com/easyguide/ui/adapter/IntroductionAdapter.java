package com.easyguide.ui.adapter;

import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easyguide.R;

import static android.text.Html.fromHtml;
import static com.google.common.base.Preconditions.checkNotNull;

public class IntroductionAdapter extends PagerAdapter {

    private ViewGroup bottomPager;

    private final String[] titles;
    private final String[] messages;

    private final String[] titlesContDes;
    private final String[] messagesContDes;

    public IntroductionAdapter(@NonNull String[] titles, @NonNull String[] messages, @NonNull String[] titlesContDes, @NonNull String[] messagesContDes, ViewGroup bottomPager) {
        checkNotNull(titles);
        checkNotNull(messages);
        checkNotNull(titlesContDes);
        checkNotNull(messagesContDes);
        this.titles = titles;
        this.messages = messages;
        this.titlesContDes = titlesContDes;
        this.messagesContDes = messagesContDes;
        this.bottomPager = bottomPager;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(container.getContext(), R.layout.adapter_introduction, null);
        TextView textViewTitle = (TextView) view.findViewById(R.id.textview_title);
        TextView textViewMessage = (TextView) view.findViewById(R.id.textview_message);
        container.addView(view, 0);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textViewMessage.setText(fromHtml(messages[position], Html.FROM_HTML_MODE_LEGACY));
        } else {
            textViewMessage.setText(fromHtml(messages[position]));
        }
        textViewMessage.setContentDescription(messagesContDes[position]);
        textViewTitle.setText(titles[position]);
        textViewTitle.setContentDescription(titlesContDes[position]);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if(bottomPager != null) {
            for (int a = 0; a < getCount(); a++) {
                View child = bottomPager.getChildAt(a);
                if (a == position) {
                    child.setBackgroundColor(0xff2ca5e0);
                } else {
                    child.setBackgroundColor(0xffbbbbbb);
                }
            }
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }
}
