package io.github.PheonixVX.ThreeDShareware.registry.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class BarrelBlock extends BlockWithEntity {
	public static final DirectionProperty FACING = Properties.FACING;
	public static final BooleanProperty OPEN = Properties.OPEN;


	public BarrelBlock(Block.Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(OPEN, false));
	}

	@Override
	public ActionResult onUse (BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.PASS;
		}
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof BarrelBlockEntity) {
			player.openHandledScreen((BarrelBlockEntity)blockEntity);
			player.increaseStat(Stats.OPEN_BARREL, 1);
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public void onStateReplaced (BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.getBlock() == newState.getBlock()) {
			return;
		}
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof Inventory) {
			ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
			world.updateComparators(pos, this);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public BlockRenderType getRenderType(BlockState blockState) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public void onPlaced (World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (itemStack.hasCustomName() && world.getBlockEntity(pos) instanceof BarrelBlockEntity) {
			((BarrelBlockEntity) world.getBlockEntity(pos)).setCustomName(itemStack.getName());
		}
	}

	@Override
	public void onEntityCollision (BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isClient && entity instanceof ProjectileEntity) {
			ProjectileEntity projectileEntity = (ProjectileEntity)entity;
			Entity theEntity = projectileEntity.getOwner();
			world.createExplosion(theEntity, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, 5.0f, Explosion.DestructionType.DESTROY);
		}
	}

	@Override
	public boolean hasComparatorOutput(BlockState blockState) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState blockState, World world, BlockPos blockPos) {
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(blockPos));
	}

	@Override
	public BlockState rotate (BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState blockState, BlockMirror mirror) {
		return blockState.rotate(mirror.getRotation(blockState.get(FACING)));
	}


	@Override
	protected void appendProperties (StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, OPEN);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
		return this.getDefaultState().with(FACING, itemPlacementContext.getPlayerFacing().getOpposite());
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return null;
	}
}
