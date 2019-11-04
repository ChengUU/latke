/*
 * Copyright (c) 2009-present, b3log.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.b3log.latke.http.renderer;

import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.b3log.latke.http.RequestContext;
import org.b3log.latke.http.Response;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.util.URLs;

import java.net.URL;

/**
 * Static file renderer.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 2.0.0.0, Nov 3, 2019
 */
public class StaticFileRenderer extends AbstractResponseRenderer {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(StaticFileRenderer.class);

    private static final Tika TIKA = new Tika();

    @Override
    public void render(final RequestContext context) {
        try {
            final Response response = context.getResponse();
            String uri = context.requestURI();
            uri = URLs.decode(uri);
            final URL resource = StaticFileRenderer.class.getResource(uri);
            if (null == resource) {
                response.sendError(404);
                return;
            }

            final String contentType = TIKA.detect(resource);
            final byte[] bytes = IOUtils.toByteArray(resource);
            response.setContentType(contentType);
            response.sendContent(bytes);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Renders static final fialed", e);
        }
    }
}
