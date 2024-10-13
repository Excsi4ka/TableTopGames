package excsi.tabletop.common.tiles;

import excsi.tabletop.gameengines.chess.Board;
import excsi.tabletop.gameengines.chess.PieceColor;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;

public class TileChessBoard extends TileEntity implements ITickableTileEntity {

    public String gameData, winner, moveList, playerWhite, playerBlack, boardStates, playerQueue;

    public PieceColor checkedColor, promotionLockedColor;

    //gameStartReInit needed due to using the same screen for starting menu and the actual game
    public boolean shouldUpdate,gameStartReInit = false,gameStarted,gameOver;

    public long timestamp = 0;

    public TileChessBoard(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
        setDefaultData();
    }

    public TileChessBoard(){
        this(ModTileEntities.tileChessBoard.get());
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putString("GameData",gameData);
        nbt.putString("MoveList",moveList);
        nbt.putString("WinnerName",winner);
        nbt.putString("PlayerQueue", playerQueue);
        nbt.putString("FirstPlayer", playerWhite);
        nbt.putString("SecondPlayer", playerBlack);
        nbt.putString("BoardStates",boardStates);
        nbt.putInt("isPromotionLocked",promotionLockedColor.ordinal());
        nbt.putInt("isChecked",checkedColor.ordinal());
        nbt.putBoolean("gameStarted",gameStarted);
        nbt.putBoolean("gameOver",gameOver);
        return super.write(nbt);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        gameData = nbt.getString("GameData");
        moveList = nbt.getString("MoveList");
        winner = nbt.getString("WinnerName");
        playerQueue = nbt.getString("PlayerQueue");
        playerWhite = nbt.getString("FirstPlayer");
        playerBlack = nbt.getString("SecondPlayer");
        boardStates = nbt.getString("BoardStates");
        promotionLockedColor = PieceColor.values()[nbt.getInt("isPromotionLocked")];
        checkedColor =  PieceColor.values()[nbt.getInt("isChecked")];
        gameStarted = nbt.getBoolean("gameStarted");
        gameOver= nbt.getBoolean("gameOver");
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket(){
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("GameData",gameData);
        nbt.putString("MoveList",moveList);
        nbt.putString("WinnerName",winner);
        nbt.putString("PlayerQueue", playerQueue);
        nbt.putString("FirstPlayer", playerWhite);
        nbt.putString("SecondPlayer", playerBlack);
        nbt.putString("BoardStates",boardStates);
        nbt.putInt("isPromotionLocked",promotionLockedColor.ordinal());
        nbt.putInt("isChecked",checkedColor.ordinal());
        nbt.putBoolean("gameStarted",gameStarted);
        nbt.putBoolean("gameOver",gameOver);
        return new SUpdateTileEntityPacket(this.pos, -1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
        CompoundNBT nbt = pkt.getNbtCompound();
        gameData = nbt.getString("GameData");
        moveList = nbt.getString("MoveList");
        winner = nbt.getString("WinnerName");
        playerQueue = nbt.getString("PlayerQueue");
        playerWhite = nbt.getString("FirstPlayer");
        playerBlack = nbt.getString("SecondPlayer");
        boardStates = nbt.getString("BoardStates");
        promotionLockedColor = PieceColor.values()[nbt.getInt("isPromotionLocked")];
        checkedColor =  PieceColor.values()[nbt.getInt("isChecked")];
        boolean flag = gameStarted;
        gameStarted = nbt.getBoolean("gameStarted");
        if(flag!=gameStarted)
            gameStartReInit = true;
        gameOver = nbt.getBoolean("gameOver");
        shouldUpdate = true;
    }

    public void setDefaultData() {
        gameData = Board.StartingFEN;
        moveList = "";
        shouldUpdate = false;
        winner = "";
        playerQueue = " ; ;";
        playerWhite = "";
        playerBlack = "";
        boardStates = "";
        promotionLockedColor = PieceColor.NONE;
        checkedColor = PieceColor.NONE;
        gameStarted = false;
        gameOver = false;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.read(state, tag);
    }

    @Override
    public void tick() {
        if(gameOver && timestamp != 0 && world.getGameTime() == timestamp) {
            setDefaultData();
            shouldUpdate = true;
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        }
    }
}
