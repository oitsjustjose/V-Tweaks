package com.oitsjustjose.vtweaks.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

/**
 * 
 * @author mezz
 * 
 *         95% of this code has been ripped from 1.11 JEI
 *
 */

public class AnvilRecipeHandler implements IRecipeHandler<AnvilRecipeWrapper>
{

	@Override
	public Class<AnvilRecipeWrapper> getRecipeClass()
	{
		return AnvilRecipeWrapper.class;
	}

	@Override
	public String getRecipeCategoryUid(AnvilRecipeWrapper recipe)
	{
		return JEICompat.getUID();
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(AnvilRecipeWrapper recipe)
	{
		return recipe;
	}

	@Override
	public boolean isRecipeValid(AnvilRecipeWrapper recipe)
	{
		return true;
	}

	@Override
	public String getRecipeCategoryUid()
	{
		return JEICompat.getUID();
	}
}