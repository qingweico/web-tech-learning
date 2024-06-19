import React from 'react';
import {Typography, Paper, Grid} from '@material-ui/core';

import {Toolbox} from './components/Toolbox';
import {SettingsPanel} from './components/SettingsPanel';
import {Topbar} from './components/Topbar';

import {Container} from './components/user/Container';
import {Button} from './components/user/Button';
import {Card, CardBottom, CardTop} from './components/user/Card';
import {Text} from './components/user/Text';
import {Editor, Frame} from "@craftjs/core";

export default function App() {
    return (
        <div>
            <Typography variant="h5" align="center">A super simple page editor</Typography>
            <Editor resolver={{Card, Button, Text, Container, CardTop, CardBottom}}>
                <Grid container spacing={3}>
                    <Grid item xs>
                        <Frame>
                            <Container padding={5} background="#eee">
                                <Card />
                                <Button size="small" variant="outlined">Click</Button>
                                <Text size="small" text="Hi world!" />
                                <Container padding={6} background="#999">
                                    <Text size="small" text="It's me again!" />
                                </Container>
                            </Container>
                        </Frame>
                    </Grid>
                    <Grid item xs={3}>
                        <Paper className={classes.root}>
                            <Toolbox />
                            <SettingsPanel />
                        </Paper>
                    </Grid>
                </Grid>
            </Editor>
        </div>
    );
}
