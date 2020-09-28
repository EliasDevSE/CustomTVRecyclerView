# CustomTVRecyclerView
This recyclerView is optimized for TVs and D-Pad remotes.
Make sure to add this library to your project first https://github.com/zhousuqiang/TvRecyclerView

Main features:

* Fast Scroll without losing focus (Works horiztonally and vertically).
* Custom calculations to leave focus on the first Item in the recyclerView while scrolling (Can be changed to any offset or center).
* Fixes multiple bugs with android's native recyclerView especially focus issues (Like losing focus when you scroll to the edges).
* Netflix Like scrolling
* Remembers last focused Vertical row item without scrolling randomly.
* Fixes this issue https://stackoverflow.com/questions/54253974/android-nested-recyclerview-scrolling-itself-when-traverse-through-parent-recyc (with the help of the persistent focus wrapper)

## Additional Notes and explanations

* In order to add netflix styled cards carousels, all you need to do is define an additional vertical recyclerView from the custom recyclerView class in your project and use the horiztonal_grid_item for singular items in rows.
* In order to change the focus offset (currently set to the first item), you need to change app:tv_selectedItemOffsetEnd="656dp" and you can also change the calculations for dx and dy to find child rectangles in the CustomRecyclerView (The function name is requestChildRectangleOnScreen). 
* As a final note, the purpose of the PersistentFocusWrapper is to fix an android AOSP bug that makes the cursor lose focus when scrolling up and down in horizontal and vertical recyclerViews.

If you require more help, feel free to make an issue.

