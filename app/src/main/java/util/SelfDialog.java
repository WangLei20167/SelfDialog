package util;

import android.app.Dialog;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mroot.selfdialog.R;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by mroot on 2018/4/28.
 */

public class SelfDialog extends Dialog {
    private ImageButton iB_back;
    private TextView titleTv;//消息标题文本
    private String titleStr;//从外界设置的title文本


    Context context;
    private onBackOnclickListener backOnclickListener;   //返回键监听

    //键盘
    private KeyboardView keyboardView;
    private Keyboard k1;// 字母键盘
    private Keyboard k2;// 数字键盘
    private Keyboard k3;// 符号键盘
    private boolean isNum = false;// 是否数据键盘
    private boolean isUpper = false;// 是否大写
    private boolean isSymbol = false;// 是否符号

    private static final int SYMBOL_CODE = -7;//符号键盘
    private static final int ELLIPSES_CODE = -8;//省略号
    private static final int CHINESE_HORIZONTAL_LINE_CODE = -9;//中文横线
    private static final int SMILING_FACE_CODE = -10;//笑脸

    private static final int HEE_CODE = -13;//哈哈
    private static final int AWKWARD_CODE = -14;//尴尬


    private ViewGroup rootView;
    private EditText ed;
private GetEidtTextIml getEidtTextListener;
    public SelfDialog(Context context) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.getEidtTextListener= (GetEidtTextIml) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_dialog);
        //安空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
    }

    private void initEvent() {
        //设置返回按钮被点击后，向外界提供监听
        iB_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backOnclickListener != null) {
                    backOnclickListener.onBack();
                }
            }
        });
    }

    private void initData() {
        //如果用户自定了title和message
        if (titleStr != null) {
            titleTv.setText(titleStr);
        }

    }

    private void initView() {

        iB_back = (ImageButton) findViewById(R.id.iB_back);

        titleTv = (TextView) findViewById(R.id.title);
        ed = (EditText) findViewById(R.id.et_password);
        getCursor();   //显示光标
        keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);

        k1 = new Keyboard(getContext(), R.xml.letter);
        k2 = new Keyboard(getContext(), R.xml.number);
        k3 = new Keyboard(getContext(), R.xml.symbol);

        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);

        keyboardView.setOnKeyboardActionListener(onKeyboardActionListener);

        rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    }

    KeyboardView.OnKeyboardActionListener onKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = ed.getText();
            int start = ed.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
                //获取到输入值
                String str=ed.getText().toString();
                Log.i("hanhai",str);
                getEidtTextListener.getEditText(str);
                onBackPressed();
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
                isUpper = !isUpper;
                k1.setShifted(isUpper);
                keyboardView.invalidateAllKeys();
            } else if (primaryCode == SYMBOL_CODE) {// 符号键盘
                if (isSymbol) {
                    isSymbol = false;
                    keyboardView.setKeyboard(k2);
                } else {
                    isSymbol = true;
                    keyboardView.setKeyboard(k3);
                }
            } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
                if (isNum) {
                    isNum = false;
                    keyboardView.setKeyboard(k1);
                } else {
                    isNum = true;
                    keyboardView.setKeyboard(k2);
                }
            } else if (primaryCode == ELLIPSES_CODE) { //省略号
                editable.insert(start, "...");
            } else if (primaryCode == CHINESE_HORIZONTAL_LINE_CODE) {
                editable.insert(start, "——");
            } else if (primaryCode == SMILING_FACE_CODE) {
                editable.insert(start, "^_^");
            } else if (primaryCode == HEE_CODE) {
                editable.insert(start, "^o^");
            } else if (primaryCode == AWKWARD_CODE) {
                editable.insert(start, ">_<");
            } else {
                String str = Character.toString((char) primaryCode);
                if (isWord(str)) {
                    if (isUpper) {
                        str = str.toUpperCase();
                    } else {
                        str = str.toLowerCase();
                    }
                }
                editable.insert(start, str);
            }
        }
    };

    //判断是否是字母
    private boolean isWord(String str) {
        return str.matches("[a-zA-Z]");
    }

    //自定义键盘后，显示光标
    private void getCursor(){
        if (android.os.Build.VERSION.SDK_INT <= 10) {//4.0以下 danielinbiti
            ed.setInputType(InputType.TYPE_NULL);
        } else {
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(ed, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        this.cancel();
    }

    /**
     * 设置返回按钮的监听
     *
     * @param onBackOnclickListener
     */
    public void setBackOnclickListener(onBackOnclickListener onBackOnclickListener) {
        this.backOnclickListener = onBackOnclickListener;
    }


    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {

    }


    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }


    //返回键监听
    public interface onBackOnclickListener {
        void onBack();
    }

}
