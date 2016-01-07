package the_fireplace.adobeblocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import the_fireplace.adobeblocks.AdobeBlocks;

import java.util.Random;

public class AdobeDoor extends Block {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool OPEN = PropertyBool.create("open");
	public static final PropertyEnum HINGE = PropertyEnum.create("hinge", BlockDoor.EnumHingePosition.class);
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	public static final PropertyEnum HALF = PropertyEnum.create("half", BlockDoor.EnumDoorHalf.class);

	public AdobeDoor() {
		super(AdobeBlocks.adobe);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, false).withProperty(HINGE, BlockDoor.EnumHingePosition.LEFT).withProperty(POWERED, false).withProperty(HALF, BlockDoor.EnumDoorHalf.LOWER));
		this.setUnlocalizedName("adobe_door");
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return isOpen(combineMetadata(worldIn, pos));
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		this.setBlockBoundsBasedOnState(worldIn, pos);
		return super.getSelectedBoundingBox(worldIn, pos);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		this.setBlockBoundsBasedOnState(worldIn, pos);
		return super.getCollisionBoundingBox(worldIn, pos, state);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		this.setBoundBasedOnMeta(combineMetadata(worldIn, pos));
	}

	private void setBoundBasedOnMeta(int combinedMeta) {
		float f = 0.1875F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
		EnumFacing enumfacing = getFacing(combinedMeta);
		boolean flag = isOpen(combinedMeta);
		boolean flag1 = isHingeLeft(combinedMeta);

		if (flag) {
			if (enumfacing == EnumFacing.EAST) {
				if (!flag1) {
					this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
				} else {
					this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
				}
			} else if (enumfacing == EnumFacing.SOUTH) {
				if (!flag1) {
					this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				} else {
					this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
				}
			} else if (enumfacing == EnumFacing.WEST) {
				if (!flag1) {
					this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
				} else {
					this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
				}
			} else if (enumfacing == EnumFacing.NORTH) {
				if (!flag1) {
					this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
				} else {
					this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				}
			}
		} else if (enumfacing == EnumFacing.EAST) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
		} else if (enumfacing == EnumFacing.SOUTH) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
		} else if (enumfacing == EnumFacing.WEST) {
			this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		} else if (enumfacing == EnumFacing.NORTH) {
			this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		BlockPos blockpos1 = state.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER ? pos : pos.down();
		IBlockState iblockstate1 = pos.equals(blockpos1) ? state : worldIn.getBlockState(blockpos1);

		if (iblockstate1.getBlock() != this) {
			return false;
		} else {
			state = iblockstate1.cycleProperty(OPEN);
			worldIn.setBlockState(blockpos1, state, 2);
			worldIn.markBlockRangeForRenderUpdate(blockpos1, pos);
			worldIn.playAuxSFXAtEntity(playerIn, (Boolean) state.getValue(OPEN) ? 1003 : 1006, pos, 0);
			return true;
		}
	}

	public void toggleDoor(World worldIn, BlockPos pos, boolean open) {
		IBlockState iblockstate = worldIn.getBlockState(pos);

		if (iblockstate.getBlock() == this) {
			BlockPos blockpos1 = iblockstate.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER ? pos : pos.down();
			IBlockState iblockstate1 = pos == blockpos1 ? iblockstate : worldIn.getBlockState(blockpos1);

			if (iblockstate1.getBlock() == this && (Boolean) iblockstate1.getValue(OPEN) != open) {
				worldIn.setBlockState(blockpos1, iblockstate1.withProperty(OPEN, open), 2);
				worldIn.markBlockRangeForRenderUpdate(blockpos1, pos);
				worldIn.playAuxSFXAtEntity((EntityPlayer) null, open ? 1003 : 1006, pos, 0);
			}
		}
	}

	/**
	 * Called when a neighboring block changes.
	 */
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER) {
			BlockPos blockpos1 = pos.down();
			IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);

			if (iblockstate1.getBlock() != this) {
				worldIn.setBlockToAir(pos);
			} else if (neighborBlock != this) {
				this.onNeighborBlockChange(worldIn, blockpos1, iblockstate1, neighborBlock);
			}
		} else {
			boolean flag1 = false;
			BlockPos blockpos2 = pos.up();
			IBlockState iblockstate2 = worldIn.getBlockState(blockpos2);

			if (iblockstate2.getBlock() != this) {
				worldIn.setBlockToAir(pos);
				flag1 = true;
			}

			if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) {
				worldIn.setBlockToAir(pos);
				flag1 = true;

				if (iblockstate2.getBlock() == this) {
					worldIn.setBlockToAir(blockpos2);
				}
			}

			if (flag1) {
				if (!worldIn.isRemote) {
					this.dropBlockAsItem(worldIn, pos, state, 0);
				}
			} else {
				boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(blockpos2);

				if ((flag || neighborBlock.canProvidePower()) && neighborBlock != this && flag != (Boolean) iblockstate2.getValue(POWERED)) {
					worldIn.setBlockState(blockpos2, iblockstate2.withProperty(POWERED, flag), 2);

					if (flag != (Boolean) state.getValue(OPEN)) {
						worldIn.setBlockState(pos, state.withProperty(OPEN, flag), 2);
						worldIn.markBlockRangeForRenderUpdate(pos, pos);
						worldIn.playAuxSFXAtEntity((EntityPlayer) null, flag ? 1003 : 1006, pos, 0);
					}
				}
			}
		}
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 *
	 * @param fortune the level of the Fortune enchantment on the player's tool
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? null : AdobeBlocks.adobe_door;
	}

	/**
	 * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit.
	 *
	 * @param start The start vector
	 * @param end   The end vector
	 */
	@Override
	public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
		this.setBlockBoundsBasedOnState(worldIn, pos);
		return super.collisionRayTrace(worldIn, pos, start, end);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return pos.getY() < worldIn.getHeight() - 1 && (World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && super.canPlaceBlockAt(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos.up()));
	}

	@Override
	public int getMobilityFlag() {
		return 1;
	}

	public static int combineMetadata(IBlockAccess worldIn, BlockPos pos) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		int i = iblockstate.getBlock().getMetaFromState(iblockstate);
		boolean flag = isTop(i);
		IBlockState iblockstate1 = worldIn.getBlockState(pos.down());
		int j = iblockstate1.getBlock().getMetaFromState(iblockstate1);
		int k = flag ? j : i;
		IBlockState iblockstate2 = worldIn.getBlockState(pos.up());
		int l = iblockstate2.getBlock().getMetaFromState(iblockstate2);
		int i1 = flag ? i : l;
		boolean flag1 = (i1 & 1) != 0;
		boolean flag2 = (i1 & 2) != 0;
		return removeHalfBit(k) | (flag ? 8 : 0) | (flag1 ? 16 : 0) | (flag2 ? 32 : 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World worldIn, BlockPos pos) {
		return AdobeBlocks.adobe_door;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		BlockPos blockpos1 = pos.down();

		if (player.capabilities.isCreativeMode && state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER && worldIn.getBlockState(blockpos1).getBlock() == this) {
			worldIn.setBlockToAir(blockpos1);
		}
	}

	/**
	 * Get the actual Block state of this Block at the given position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		IBlockState iblockstate1;

		if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER) {
			iblockstate1 = worldIn.getBlockState(pos.up());

			if (iblockstate1.getBlock() == this) {
				state = state.withProperty(HINGE, iblockstate1.getValue(HINGE)).withProperty(POWERED, iblockstate1.getValue(POWERED));
			}
		} else {
			iblockstate1 = worldIn.getBlockState(pos.down());

			if (iblockstate1.getBlock() == this) {
				state = state.withProperty(FACING, iblockstate1.getValue(FACING)).withProperty(OPEN, iblockstate1.getValue(OPEN));
			}
		}

		return state;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return (meta & 8) > 0 ? this.getDefaultState().withProperty(HALF, BlockDoor.EnumDoorHalf.UPPER).withProperty(HINGE, (meta & 1) > 0 ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT).withProperty(POWERED, (meta & 2) > 0) : this.getDefaultState().withProperty(HALF, BlockDoor.EnumDoorHalf.LOWER).withProperty(FACING, EnumFacing.getHorizontal(meta & 3).rotateYCCW()).withProperty(OPEN, (meta & 4) > 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		byte b0 = 0;
		int i;

		if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER) {
			i = b0 | 8;

			if (state.getValue(HINGE) == BlockDoor.EnumHingePosition.RIGHT) {
				i |= 1;
			}

			if ((Boolean) state.getValue(POWERED)) {
				i |= 2;
			}
		} else {
			i = b0 | ((EnumFacing) state.getValue(FACING)).rotateY().getHorizontalIndex();

			if ((Boolean) state.getValue(OPEN)) {
				i |= 4;
			}
		}

		return i;
	}

	protected static int removeHalfBit(int meta) {
		return meta & 7;
	}

	public static boolean isOpen(IBlockAccess worldIn, BlockPos pos) {
		return isOpen(combineMetadata(worldIn, pos));
	}

	public static EnumFacing getFacing(IBlockAccess worldIn, BlockPos pos) {
		return getFacing(combineMetadata(worldIn, pos));
	}

	public static EnumFacing getFacing(int combinedMeta) {
		return EnumFacing.getHorizontal(combinedMeta & 3).rotateYCCW();
	}

	protected static boolean isOpen(int combinedMeta) {
		return (combinedMeta & 4) != 0;
	}

	protected static boolean isTop(int meta) {
		return (meta & 8) != 0;
	}

	protected static boolean isHingeLeft(int combinedMeta) {
		return (combinedMeta & 16) != 0;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[]{HALF, FACING, OPEN, HINGE, POWERED});
	}

	public enum EnumDoorHalf implements IStringSerializable {
		UPPER,
		LOWER;

		@Override
		public String toString() {
			return this.getName();
		}

		@Override
		public String getName() {
			return this == UPPER ? "upper" : "lower";
		}
	}

	public enum EnumHingePosition implements IStringSerializable {
		LEFT,
		RIGHT;

		@Override
		public String toString() {
			return this.getName();
		}

		@Override
		public String getName() {
			return this == LEFT ? "left" : "right";
		}
	}
}
