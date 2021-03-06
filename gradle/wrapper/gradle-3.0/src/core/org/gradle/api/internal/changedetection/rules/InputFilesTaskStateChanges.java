/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.changedetection.rules;

import org.gradle.api.internal.TaskInternal;
import org.gradle.api.internal.changedetection.state.FileCollectionSnapshot;
import org.gradle.api.internal.changedetection.state.FileCollectionSnapshotter;
import org.gradle.api.internal.changedetection.state.TaskExecution;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class InputFilesTaskStateChanges extends AbstractNamedFileSnapshotTaskStateChanges {
    public InputFilesTaskStateChanges(TaskExecution previous, TaskExecution current, TaskInternal task, FileCollectionSnapshotter snapshotter) {
        super(task.getName(), previous, current, snapshotter, true, "Input", task.getInputs().getFileProperties());
        // Inputs are considered to be unchanged during task execution
        current.setInputFilesSnapshot(getCurrent());
    }

    @Override
    protected Map<String, FileCollectionSnapshot> getPrevious() {
        return previous.getInputFilesSnapshot();
    }

    @Override
    public void saveCurrent() {
        // Inputs have already been saved in constructor
    }

    @Override
    protected Set<FileCollectionSnapshot.ChangeFilter> getFileChangeFilters() {
        return Collections.emptySet();
    }
}
