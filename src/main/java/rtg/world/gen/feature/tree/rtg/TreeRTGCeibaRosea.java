package rtg.world.gen.feature.tree.rtg;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import rtg.config.rtg.ConfigRTG;

public class TreeRTGCeibaRosea extends TreeRTG
{

	public float length;
	public int branch;
	public float verStart;
	public float verRand;

	public TreeRTGCeibaRosea()
	{
		super();

		length = 16f;
		branch = 5;
		verStart = 0.32f;
		verRand = 0.1f;
	}
	
	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) 
	{
    	Block b = world.getBlock(x, y - 1, z);
    	
        if (b == Blocks.sand && !ConfigRTG.allowTreesToGenerateOnSand) {
            return false;
        }
    	
    	if(b != Blocks.grass && b != Blocks.dirt && b != Blocks.sand)
    	{
    		return false;
    	}
    	
    	float r = rand.nextFloat() * 360;
    	if(this.trunkSize > 0)
    	{
	    	for(int k = 0; k < 5; k++)
	    	{
	    		generateBranch(world, rand, (float)x + 0.5f, y + this.trunkSize, (float)z + 0.5f, (120 * k) - 25 + rand.nextInt(50) + r, 1.6f + rand.nextFloat() * 0.1f, this.trunkSize * 1.8f, 1f);
	    	}
    	}
    	
    	for(int i = y + this.trunkSize - 2; i < y + this.crownSize + 2; i++)
    	{
    		world.setBlock(x, i, z, this.logBlock, this.logMeta, 2);
    		world.setBlock(x + 1, i, z + 1, this.logBlock, this.logMeta, 2);
    	}
    	
    	float horDir, verDir;
    	int eX, eY, eZ;
    	for(int j = 0; j < branch; j++)
    	{
    		horDir = (80 * j) - 40 + rand.nextInt(80);
    		verDir = verStart + rand.nextFloat() * verRand;
        	generateBranch(world, rand, (float)x + 0.5f, y + this.crownSize, (float)z + 0.5f, horDir, verDir, length, 1f);
        	
        	eX = (int)(Math.cos(horDir * Math.PI / 180D) * verDir * length);
        	eZ = (int)(Math.sin(horDir * Math.PI / 180D) * verDir * length);
        	eY = (int)((1f - verDir) * length);
        	
        	for(int m = 0; m < 2; m++)
        	{
            	generateLeaves(world, rand, x + eX - 2 + rand.nextInt(5), y + this.crownSize + eY + rand.nextInt(2), z + eZ - 2 + rand.nextInt(5), 4f, 1.5f);
        	}
        	
        	eX *= 0.8f;
        	eY *= 0.8f;
        	eZ *= 0.8f;
        	
        	generateLeaves(world, rand, x + eX, y + this.crownSize + eY, z + eZ, 3f, 1.5f);
    	}
		
		return true;
	}
	
	/*
	 * horDir = number between -180D and 180D
	 * verDir = number between 1F (horizontal) and 0F (vertical)
	 */
	public void generateBranch(World world, Random rand, float x, float y, float z, double horDir, float verDir, float length, float speed)
	{
		if(verDir < 0f)
		{
			verDir = -verDir;
		}

		float c = 0f;
		float velY = 1f - verDir;
		
		if(verDir > 1f)
		{
			verDir = 1f - (verDir - 1f);
		}
		
		float velX = (float)Math.cos(horDir * Math.PI / 180D) * verDir;
		float velZ = (float)Math.sin(horDir * Math.PI / 180D) * verDir;
		
		while(c < length)
		{
			world.setBlock((int)x, (int)y, (int)z, this.logBlock, this.logMeta, 2);
			
			x += velX;
			y += velY;
			z += velZ;
			
			c += speed;
		}
	}
	
	public void generateLeaves(World world, Random rand, float x, float y, float z, float size, float width)
	{
		float dist;
		int i, j, k, s = (int)(size - 1f), w = (int)((size - 1f) * width);
		for(i = -w; i <= w; i++)
		{
			for(j = -s + 1; j <= s; j++)
			{
				for(k = -w; k <= w; k++)
				{
					dist = Math.abs((float)i / width) + (float)Math.abs(j) + Math.abs((float)k / width);
					if(dist <= size - 0.5f || (dist <= size && rand.nextBoolean()))
					{
						if(dist < 1.3f)
						{
							world.setBlock((int)x + i, (int)y + j, (int)z + k, this.logBlock, this.logMeta, 2);
						}
						if(world.isAirBlock((int)x + i, (int)y + j, (int)z + k))
						{
							world.setBlock((int)x + i, (int)y + j, (int)z + k, this.leavesBlock, this.leavesMeta, 2);
						}
					}
				}
			}
		}
	}
}
