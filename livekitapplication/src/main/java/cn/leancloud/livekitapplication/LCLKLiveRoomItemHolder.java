package cn.leancloud.livekitapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVCallback;
import com.avos.avoscloud.AVException;
import com.squareup.picasso.Picasso;

import cn.leancloud.leancloudlivekit.LCLKUser;
import cn.leancloud.leancloudlivekit.im.LCLKCommonViewHolder;
import cn.leancloud.leancloudlivekit.im.LCLKRoundCornerImageView;
import cn.leancloud.leancloudlivekit.utils.LCLKProfileCache;
import de.greenrobot.event.EventBus;

/**
 * Created by wli on 16/8/5.
 */
public class LCLKLiveRoomItemHolder extends LCLKCommonViewHolder<LCLiveRoom> {

  private ImageView backgroudView;
  private TextView nameView;
  private LCLKRoundCornerImageView avatarView;
  private TextView statusView;
  private TextView descriptionView;

  private LCLiveRoom liveRoom;

  public LCLKLiveRoomItemHolder(Context context, ViewGroup root) {
    super(context, root, R.layout.lclk_live_room_item);
    backgroudView = (ImageView) itemView.findViewById(R.id.live_room_item_bg_view);
    nameView = (TextView) itemView.findViewById(R.id.live_room_item_name_view);
    avatarView = (LCLKRoundCornerImageView) itemView.findViewById(R.id.live_room_item_avatar_view);
    statusView = (TextView) itemView.findViewById(R.id.live_room_item_status_view);
    descriptionView = (TextView) itemView.findViewById(R.id.live_room_item_description_view);

    itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        EventBus.getDefault().post(new LCLKLiveRoomItemClickEvent(liveRoom));
      }
    });
  }

  @Override
  public void bindData(LCLiveRoom liveRoom) {
    this.liveRoom = liveRoom;
    if(liveRoom.getConver() != null) {
      Picasso.with(getContext()).load(liveRoom.getConver().getUrl()).into(backgroudView);
    }else {
      Picasso.with(getContext()).load(R.mipmap.lclk_live_add_image).into(backgroudView);
    }
    LCLKProfileCache.getInstance().getCachedUser(liveRoom.getAnchorId(), new AVCallback<LCLKUser>() {
      @Override
      protected void internalDone0(LCLKUser lclkUser, AVException e) {
        if(lclkUser != null) {
          nameView.setText(lclkUser.getUserName());
          Picasso.with(getContext()).load(lclkUser.getAvatarUrl()).into(avatarView);
        }else {
          nameView.setText(R.string.lclk_live_room_unknow_name);
          Picasso.with(getContext()).load(R.mipmap.ic_launcher).into(avatarView);
        }
      }
    });
    descriptionView.setText(liveRoom.getTitle() + " #" + liveRoom.getTopic());
  }

  public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<LCLKLiveRoomItemHolder>() {
    @Override
    public LCLKLiveRoomItemHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
      return new LCLKLiveRoomItemHolder(parent.getContext(), parent);
    }
  };
}
