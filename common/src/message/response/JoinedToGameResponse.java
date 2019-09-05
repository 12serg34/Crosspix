package message.response;

import picture.StashedPicture;

public class JoinedToGameResponse implements Response {
    private static final long serialVersionUID = 7873540238231988321L;
    private final StashedPicture stashedPicture;

    public JoinedToGameResponse(StashedPicture stashedPicture) {
        this.stashedPicture = stashedPicture;
    }

    public StashedPicture getStashedPicture() {
        return stashedPicture;
    }
}
