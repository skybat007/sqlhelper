
/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the LGPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.sqlhelper.dialect.pagination;

import com.jn.sqlhelper.dialect.RowSelection;
import com.jn.sqlhelper.dialect.RowSelectionBuilder;
import com.jn.sqlhelper.dialect.conf.Settings;

public class PagingRequestBasedRowSelectionBuilder implements RowSelectionBuilder<PagingRequest> {
    private int defaultPageSize = Settings.getInstance().getPageSize();

    @Override
    public RowSelection build(PagingRequest request)
            throws IllegalArgumentException {
        if (request.isValidRequest()) {
            RowSelection rowSelection = new RowSelection();
            rowSelection.setFetchSize(request.getFetchSize());
            rowSelection.setTimeout(request.getTimeout());

            int pageNo = request.getPageNo();
            long offset = 0L;
            int limit = request.getPageSize();
            if (request.isGetAllFromNonZeroOffsetRequest()) {
                offset = (pageNo - 1) * getDefaultPageSize();
                limit = Integer.MAX_VALUE;
            } else {
                offset = pageNo > 0 ? (pageNo - 1) * request.getPageSize() : 0;
            }
            rowSelection.setLimit(limit);
            if (offset - 1 + rowSelection.getLimit() > Integer.MAX_VALUE) {
                rowSelection.setFetchSize(Integer.MAX_VALUE - Long.valueOf(offset).intValue());
            }
            rowSelection.setOffset(offset);

            rowSelection.setMaxRows(request.getMaxRows());
            return rowSelection;
        }
        throw new IllegalArgumentException("PagingRequest is illegal");
    }

    public int getDefaultPageSize() {
        return defaultPageSize;
    }

    public void setDefaultPageSize(int defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }
}

