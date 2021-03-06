/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ambari.view.huetoambarimigration.migration;

import com.google.inject.Inject;
import org.apache.ambari.view.ViewContext;
import org.apache.ambari.view.huetoambarimigration.resources.PersonalCRUDResourceManager;
import org.apache.ambari.view.huetoambarimigration.resources.scripts.MigrationResourceManager;
import org.apache.ambari.view.huetoambarimigration.resources.scripts.models.MigrationResponse;
import org.apache.ambari.view.huetoambarimigration.migration.revertchange.RevertChangeStartJob;
import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Path("/startrevertchanges")

public class InitiateJobMigrationforRevertchange implements Runnable{

  MigrationResponse migrationresult=new MigrationResponse();

  public void run(){

  }


  @Inject
  ViewContext view;


  protected MigrationResourceManager resourceManager = null;

  public synchronized PersonalCRUDResourceManager<MigrationResponse> getResourceManager() {
    if (resourceManager == null) {
      resourceManager = new MigrationResourceManager(view);
    }
    return resourceManager;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)

  public Response RevertChangeJob(@QueryParam("instance") String instance,@QueryParam("revertdate") String revertdate,@QueryParam("jobid") String jobid) throws IOException, InvocationTargetException, IllegalAccessException {

    JSONObject response = new JSONObject();

    new RevertChangeStartJob(instance,revertdate,jobid, view).start();

    migrationresult.setId(jobid);
    migrationresult.setProgressPercentage(0);
    migrationresult.setJobtype("revertchange");
    migrationresult.setIntanceName(instance);

    new RevertChangeStartJob(instance,revertdate,jobid, view).start();

    response.put("startrevertchanges",migrationresult);

    return Response.ok(response).build();

  }



}
