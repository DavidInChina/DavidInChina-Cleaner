package com.wechat.files.cleaner.view

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wechat.files.cleaner.R
import com.wechat.files.cleaner.data.bean.WechatFile
import com.wechat.files.cleaner.utils.DeviceUtils
import com.wechat.files.cleaner.utils.MemoryInfoUtil
import kotlinx.android.synthetic.main.diag_large_file_detail.*
import java.text.DateFormat

/**
 * @fileName LargeFileDetailDialog
 * @date 2019/5/14
 * @author andrew li
 * @desc
 */
class WechatFileDetailDialog: DialogFragment() {
    private var wechatFile: WechatFile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.RatingDialog)
        isCancelable = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return activity?.layoutInflater?.inflate(R.layout.diag_large_file_detail, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        wechatFile = arguments?.getParcelable(EXTRA_LARGE_FILE)

        tvFileName.text = wechatFile?.filePath
        tvDate.text = DateFormat.getDateInstance().format(wechatFile?.modifyTime ?: 0L)
        tvSizeInfo.text = MemoryInfoUtil.formatMemorySize(wechatFile?.fileSize ?: 0L)
        tvPath.text = wechatFile?.filePath ?: ""

        tvCancel.setOnClickListener {
            dismiss()
        }

        tvView.setOnClickListener {
            wechatFile?.preview(context as Context)
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog.window!!.attributes
        params.width = DeviceUtils.dp2px(context, 280F)
        params.height = DeviceUtils.dp2px(context, 248F)
        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    companion object {
        private val TAG: String = WechatFileDetailDialog::class.java.simpleName
        private const val EXTRA_LARGE_FILE = "extra_large_file"

         fun newInstance(wechatFile: WechatFile): WechatFileDetailDialog {
            val dlg = WechatFileDetailDialog()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_LARGE_FILE, wechatFile)
            dlg.arguments = bundle
            return dlg
        }
    }
}