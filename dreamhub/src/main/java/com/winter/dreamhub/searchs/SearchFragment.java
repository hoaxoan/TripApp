package com.winter.dreamhub.searchs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.service.WinterService;

import java.util.ArrayList;
import java.util.List;

import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * Created by hoaxoan on 12/14/2016.
 */

public class SearchFragment extends Fragment {
    public static final String TAG = makeLogTag(SearchFragment.class);
    private View rootView;

    private GridView quickSearchView;
    private EditText searchView;

    private List<QuickSearch> searchs = new ArrayList<>();

    /**
     * Create a new instance of the fragment
     */
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.searchView = (EditText) this.rootView.findViewById(R.id.search_box);
        this.searchView.setOnClickListener(mOnClickListener);
        this.searchView.addTextChangedListener(mTextWatcher);
        this.searchView.setOnEditorActionListener(mOnEditorActionListener);
        this.searchView.setOnKeyListener(mTextKeyListener);

        this.init();
        this.quickSearchView = (GridView) this.rootView.findViewById(R.id.gvExploreQuickSearch);
        this.quickSearchView.setAdapter(new QuickSearchAdapter(getActivity(), searchs));
        this.quickSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), SearchListActivity.class);
                if(position >= 0 && position < searchs.size()){
                    QuickSearch search = searchs.get(position);
                    intent.putExtra("type", search.type);
                }
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.search_fragment, container, false);
        return this.rootView;
    }

    @Override
    public final void onPause() {
        super.onPause();
    }

    @Override
    public final void onResume() {
        super.onResume();
    }

    private void startSearchActivity() {
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == searchView) {
                startSearchActivity();
            }
        }
    };

    /**
     * Callback to watch the text field for empty/non-empty
     */
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int before, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int after) {
            startSearchActivity();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private final TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {

        /**
         * Called when the input method default action key is pressed.
         */
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            startSearchActivity();
            return true;
        }
    };

    /**
     * React to the user typing "enter" or other hardwired keys while typing in
     * the search box. This handles these special keys while the edit box has
     * focus.
     */
    View.OnKeyListener mTextKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // If there is text in the query box, handle enter, and action keys
            // The search key is handled by the dialog's onKeyDown().
            if (event.hasNoModifiers()) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        v.cancelLongPress();

                        startSearchActivity();
                        return true;
                    }
                }
            }
            return false;
        }
    };

    public class QuickSearchAdapter extends BaseAdapter {
        private Context mContext;
        private List<QuickSearch> mSearchs;

        public QuickSearchAdapter(Context context, List<QuickSearch> searchs) {
            this.mContext = context;
            this.mSearchs = searchs;
        }

        @Override
        public int getCount() {
            return mSearchs.size();
        }

        @Override
        public Object getItem(int i) {
            return mSearchs.get(i);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View convertView;
            if (view == null) {
                convertView = new View(mContext);
                convertView = inflater.inflate(R.layout.grid_item_search, null);
                ImageView icon = (ImageView) convertView.findViewById(R.id.ivIcon);
                TextView name = (TextView) convertView.findViewById(R.id.tvName);

                if(i >= 0 && i < mSearchs.size()){
                    QuickSearch search = mSearchs.get(i);
                    icon.setImageResource(search.thumbId);
                    name.setText(mContext.getString(search.termId));
                }
            } else {
                convertView = (View) view;
            }

            return convertView;
        }
    }

    private void init(){
        searchs.add(new QuickSearch(R.drawable.breakfast_intent, R.string.breakfast, WinterService.TYPE_LANDMARKS));
        searchs.add(new QuickSearch(R.drawable.lunch_intent, R.string.lunch, WinterService.TYPE_LANDMARKS));
        searchs.add(new QuickSearch(R.drawable.dinner_intent, R.string.dinner, WinterService.TYPE_LANDMARKS));
        searchs.add(new QuickSearch(R.drawable.coffeetea_intent, R.string.coffeetea, WinterService.TYPE_RESTAURANTS));
        searchs.add(new QuickSearch(R.drawable.nightlife_intent, R.string.nightlife, WinterService.TYPE_HOTELS));
        searchs.add(new QuickSearch(R.drawable.thingstodo_intent, R.string.things_to_do, WinterService.TYPE_LANDMARKS));
    }

    private class QuickSearch {
        public int thumbId;
        public int termId;
        public int type;

        public QuickSearch(int thumbId, int termId, int type) {
            this.thumbId = thumbId;
            this.termId = termId;
            this.type = type;
        }
    }

}
