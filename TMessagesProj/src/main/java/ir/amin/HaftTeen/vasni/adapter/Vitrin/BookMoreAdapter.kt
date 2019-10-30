package ir.amin.HaftTeen.vasni.adapter.Vitrin

import android.view.View
import kotlinx.android.synthetic.main.row_media_book_more.*
import me.himanshusoni.chatmessageview.ui.MoreView.MoreViewHolder
import ir.amin.HaftTeen.ui.LaunchActivity.presentFragment
import ir.amin.HaftTeen.vasni.core.DataLoader
import ir.amin.HaftTeen.vasni.extention.loadImage
import ir.amin.HaftTeen.vasni.model.teentaak.Vitrin.Book
import ir.amin.HaftTeen.vasni.teentaak.fragment.market.BookDetailFragment
import ir.amin.HaftTeen.R


class BookMoreAdapter(containerView: View) : MoreViewHolder<Book>(containerView) {

    override fun bindData(data: Book, payloads: List<Any>) {

        imv_media_book_more.loadImage(containerView.context, data.thumbnail!!, pv_loading_pic_book_more)
        tv_media_book_more_name.text = data.title
        if (data.price == 0)
            tv_media_book_more_price.text = containerView.context.getString(R.string.free)
        else
            tv_media_book_more_price.text =
                    data.price.toString() + " " + containerView.context.getString(R.string.currency)

        ll_book_more.setOnClickListener {
            DataLoader.instance.book = data
            presentFragment(BookDetailFragment(""))
        }

    }

}