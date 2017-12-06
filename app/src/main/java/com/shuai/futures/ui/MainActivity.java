package com.shuai.futures.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.shuai.futures.MyApplication;
import com.shuai.futures.R;
import com.shuai.futures.data.Config;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.LoginStateChanged;
import com.shuai.futures.logic.UserManager;
import com.shuai.futures.net.ConnectionChangeMonitor;
import com.shuai.futures.ui.base.BaseFragmentActivity;
import com.shuai.futures.utils.NavigateUtils;
import java.util.ArrayList;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

import static android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;


public class MainActivity extends BaseFragmentActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext;
    private RequestQueue mRequestQueue;

    /**
     * 上次按下back按钮的时间
     */
    private long mLastBackPressedTime;

    private ViewPager mViewPager;
    private TabHost mTabHost;
    private TabsAdapter mTabsAdapter;

    public class TabsAdapter extends FragmentPagerAdapter implements TabHost.OnTabChangeListener,
            ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
        private final Fragment[] mFragments = new Fragment[4];

        final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabsAdapter(FragmentActivity activity) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args, boolean b) {
            tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);

            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            if (mFragments[position] == null) {
                mFragments[position] = Fragment.instantiate(mContext, info.clss.getName(), info.args);
            }
            return mFragments[position];
        }

        @Override
        public void onTabChanged(String tabId) {
            int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        super.onCreate(savedInstanceState);

        mRequestQueue = MyApplication.getRequestQueue();
        setContentView(R.layout.activity_main);

        //自动更新
//        UmengUpdateAgent.update(this);
//        //UmengUpdateAgent.silentUpdate(this);静默下载更新
//        UmengUpdateAgent.setUpdateOnlyWifi(false);

//        if(!UserManager.getInstance().isLogined())
//            UserManager.getInstance().autoLogin();

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mViewPager = (ViewPager) findViewById(R.id.pager);

        mTabsAdapter = new TabsAdapter(this);

        createTabs();

        //防止回收被隐藏的page
        mViewPager.setOffscreenPageLimit(mTabsAdapter.getCount());
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Config.getInstance().saveConfig();
        mRequestQueue.cancelAll(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!getSupportFragmentManager().popBackStackImmediate()) {
            if (System.currentTimeMillis() - mLastBackPressedTime > 2000) {
                mLastBackPressedTime = System.currentTimeMillis();
                Toast.makeText(mContext, R.string.one_more_exit, Toast.LENGTH_SHORT).show();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
//        /** 使用SSO授权必须添加如下代码 */
//        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
//        if (ssoHandler != null) {
//            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
//        }
    }

    /**
     * 创建maintab
     */
    private void createTabs() {
        String[] tabIds = {"followed", "market", "news", "user"};
        int[] titles = {R.string.tab_followed, R.string.tab_market, R.string.tab_news, R.string.tab_user};
        int[] icons = {R.drawable.tab_followed, R.drawable.tab_market, R.drawable.tab_news, R.drawable.tab_user};
        Class<?>[] fragments = {FollowFragment.class, MarketFragment.class, NewsFragment.class,
                UserCenterFragment.class};

        LayoutInflater inflater = getLayoutInflater();
        for (int i = 0; i < tabIds.length; i++) {
            View tab = inflater.inflate(R.layout.tab_with_icon, null);
            tab.setBackgroundColor(0xfff);
            TextView title = (TextView) tab.findViewById(R.id.tab_title);
            title.setText(titles[i]);
            ImageView iv = (ImageView) tab.findViewById(R.id.tab_icon);
            iv.setImageResource(icons[i]);
            mTabsAdapter.addTab(mTabHost.newTabSpec(tabIds[i]).setIndicator(tab), fragments[i], null, false);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showTab(intent.getIntExtra(Constants.EXTRA_TAB, NavigateUtils.TAB_FOLLOWED));
    }

    private void showTab(int tab) {
        switch (tab) {
            case NavigateUtils.TAB_FOLLOWED: {
                mViewPager.setCurrentItem(0);
                break;
            }
            case NavigateUtils.TAB_MARKET: {
                mViewPager.setCurrentItem(1);
                break;
            }
            case NavigateUtils.TAB_NEWS: {
                mViewPager.setCurrentItem(2);
                break;
            }
            case NavigateUtils.TAB_USER: {
                if (!isLogined()) {
                    //NavigateUtils.showTab(mContext, NavigateUtils.TAB_MESSAGE);
                    return;
                }
                mViewPager.setCurrentItem(3);
                break;
            }
        }
    }

    private boolean isLogined() {
        return UserManager.getInstance().isLogined();
    }

    @Subscribe
    public void onEvent(LoginStateChanged state) {
        if (isLogined()) {
            //登陆成功，自动获取用户信息
//            GetUserProfileTask request=new GetUserProfileTask(mContext, null);
//            request.setTag(this);
//            mRequestQueue.add(request);
        } else {
            showTab(NavigateUtils.TAB_FOLLOWED);
        }
    }

    @Subscribe
    public void onEvent(ConnectionChangeMonitor.EventConnectionChange event) {
        if (event.isConnected()) {
            //发现连上网，自动登录
            if (!UserManager.getInstance().isLogined())
                UserManager.getInstance().autoLogin();
        }
    }

}

